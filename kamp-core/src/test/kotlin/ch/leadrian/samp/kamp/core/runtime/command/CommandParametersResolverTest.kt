package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandParameterDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.DefaultInvalidCommandParameterValueHandler
import ch.leadrian.samp.kamp.core.api.command.InvalidCommandParameterValueHandler
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class CommandParametersResolverTest {

    private lateinit var commandParametersResolver: CommandParametersResolver

    private val defaultInvalidCommandParameterValueHandler = mockk<DefaultInvalidCommandParameterValueHandler>()
    private val method = Any::class.java.getMethod("hashCode")
    private val player = mockk<Player>()

    @BeforeEach
    fun setUp() {
        commandParametersResolver = CommandParametersResolver(defaultInvalidCommandParameterValueHandler)
    }

    @Test
    fun givenLessParameterValuesInThanExpectedItShouldExecuteInvalidCommandParameterValueHandler() {
        every {
            defaultInvalidCommandParameterValueHandler.handle(any(), any(), any(), any())
        } returns OnPlayerCommandTextListener.Result.Processed
        val parameter1 = mockk<CommandParameterDefinition>()
        val parameter2 = mockk<CommandParameterDefinition>()
        val parameter3 = mockk<CommandParameterDefinition>()
        val stringParameterValues = listOf("lol", "Hi there")
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                commandsInstance = TestCommands,
                parameters = listOf(parameter1, parameter2, parameter3)
        )

        val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)

        assertThat(result)
                .isInstanceOfSatisfying(CommandParametersResolver.Result.Error::class.java) {
                    assertThat(it.returnValue)
                            .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
                }
        verify {
            defaultInvalidCommandParameterValueHandler.handle(player, commandDefinition, stringParameterValues, 2)
        }
    }

    @Test
    fun shouldResolveSimpleParameters() {
        val floatResolver = mockk<CommandParameterResolver<Float>> {
            every { resolve("1.23") } returns 1.23f
        }
        val intResolver = mockk<CommandParameterResolver<Int>> {
            every { resolve("123") } returns 123
        }
        val parameter1 = mockk<CommandParameterDefinition> {
            every { resolver } returns floatResolver
            every { type } returns Float::class.java
        }
        val parameter2 = mockk<CommandParameterDefinition> {
            every { resolver } returns intResolver
            every { type } returns Int::class.java
        }
        val stringParameterValues = listOf("1.23", "123")
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                commandsInstance = TestCommands,
                parameters = listOf(parameter1, parameter2)
        )

        val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)

        assertThat(result)
                .isInstanceOfSatisfying(CommandParametersResolver.Result.ParameterValues::class.java) {
                    assertThat(it.parameterValues)
                            .containsExactly(player, 1.23f, 123)
                }
    }

    @Test
    fun shouldResolveGreedyStringParameter() {
        val stringResolver = mockk<CommandParameterResolver<String>> {
            every { resolve("lol") } returns "lol"
            every { resolve("rofl hahaha 1234") } returns "rofl hahaha 1234"
        }
        val parameter1 = mockk<CommandParameterDefinition> {
            every { resolver } returns stringResolver
            every { type } returns String::class.java
        }
        val parameter2 = mockk<CommandParameterDefinition> {
            every { resolver } returns stringResolver
            every { type } returns String::class.java
        }
        val stringParameterValues = listOf("lol", "rofl", "hahaha", "1234")
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                commandsInstance = TestCommands,
                parameters = listOf(parameter1, parameter2),
                isGreedy = true
        )

        val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)

        assertThat(result)
                .isInstanceOfSatisfying(CommandParametersResolver.Result.ParameterValues::class.java) {
                    assertThat(it.parameterValues)
                            .containsExactly(player, "lol", "rofl hahaha 1234")
                }
    }

    @Test
    fun shouldResolveNonGreedyStringParameter() {
        val stringResolver = mockk<CommandParameterResolver<String>> {
            every { resolve("lol") } returns "lol"
            every { resolve("rofl") } returns "rofl"
        }
        val parameter1 = mockk<CommandParameterDefinition> {
            every { resolver } returns stringResolver
            every { type } returns String::class.java
        }
        val parameter2 = mockk<CommandParameterDefinition> {
            every { resolver } returns stringResolver
            every { type } returns String::class.java
        }
        val stringParameterValues = listOf("lol", "rofl", "hahaha", "1234")
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                commandsInstance = TestCommands,
                parameters = listOf(parameter1, parameter2),
                isGreedy = false
        )

        val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)

        assertThat(result)
                .isInstanceOfSatisfying(CommandParametersResolver.Result.ParameterValues::class.java) {
                    assertThat(it.parameterValues)
                            .containsExactly(player, "lol", "rofl")
                }
    }

    @Nested
    inner class ResolutionToListTests {

        @ParameterizedTest
        @ArgumentsSource(ResolutionToListArgumentsProvider::class)
        fun shouldResolveParametersToList(lastParameterType: Class<*>) {
            val stringResolver = mockk<CommandParameterResolver<String>> {
                every { resolve("lol") } returns "lol"
                every { resolve("rofl") } returns "rofl"
                every { resolve("hahaha") } returns "hahaha"
                every { resolve("1234") } returns "1234"
            }
            val parameter1 = mockk<CommandParameterDefinition> {
                every { resolver } returns stringResolver
                every { type } returns String::class.java
            }
            val parameter2 = mockk<CommandParameterDefinition> {
                every { resolver } returns stringResolver
                every { type } returns lastParameterType
            }
            val stringParameterValues = listOf("lol", "rofl", "hahaha", "1234")
            val commandDefinition = CommandDefinition(
                    name = "foo",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf(parameter1, parameter2),
                    isGreedy = false
            )

            val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)

            assertThat(result)
                    .isInstanceOfSatisfying(CommandParametersResolver.Result.ParameterValues::class.java) {
                        assertThat(it.parameterValues)
                                .containsExactly(player, "lol", listOf("rofl", "hahaha", "1234"))
                    }
        }
    }

    @Test
    fun shouldResolveSetOfStringsParameter() {
        val stringResolver = mockk<CommandParameterResolver<String>> {
            every { resolve("lol") } returns "lol"
            every { resolve("rofl") } returns "rofl"
            every { resolve("hahaha") } returns "hahaha"
            every { resolve("1234") } returns "1234"
        }
        val parameter1 = mockk<CommandParameterDefinition> {
            every { resolver } returns stringResolver
            every { type } returns String::class.java
        }
        val parameter2 = mockk<CommandParameterDefinition> {
            every { resolver } returns stringResolver
            every { type } returns Set::class.java
        }
        val stringParameterValues = listOf("lol", "rofl", "hahaha", "1234")
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                commandsInstance = TestCommands,
                parameters = listOf(parameter1, parameter2),
                isGreedy = false
        )

        val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)

        assertThat(result)
                .isInstanceOfSatisfying(CommandParametersResolver.Result.ParameterValues::class.java) {
                    assertThat(it.parameterValues)
                            .containsExactly(player, "lol", setOf("rofl", "hahaha", "1234"))
                }
    }

    @Nested
    inner class InvalidParameterValueTests {

        @ParameterizedTest
        @ArgumentsSource(InvalidCollectionTypeParameterValueArgumentsProvider::class)
        fun givenInvalidParameterItShouldExecuteDefaultInvalidValueHandler(lastParameterType: Class<*>) {
            val stringResolver = mockk<CommandParameterResolver<String>> {
                every { resolve("lol") } returns "lol"
                every { resolve("rofl") } returns "rofl"
                every { resolve("hahaha") } returns null
            }
            val parameter1 = mockk<CommandParameterDefinition> {
                every { resolver } returns stringResolver
                every { type } returns String::class.java
            }
            val parameter2 = mockk<CommandParameterDefinition> {
                every { resolver } returns stringResolver
                every { type } returns lastParameterType
                every { invalidCommandParameterValueHandler } returns null
            }
            every {
                defaultInvalidCommandParameterValueHandler.handle(any(), any(), any(), any())
            } returns OnPlayerCommandTextListener.Result.Processed
            val stringParameterValues = listOf("lol", "rofl", "hahaha", "1234")
            val commandDefinition = CommandDefinition(
                    name = "foo",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf(parameter1, parameter2),
                    isGreedy = false
            )

            val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)

            assertThat(result)
                    .isInstanceOfSatisfying(CommandParametersResolver.Result.Error::class.java) {
                        assertThat(it.returnValue)
                                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
                    }
            verify {
                defaultInvalidCommandParameterValueHandler.handle(player, commandDefinition, stringParameterValues, 1)
            }
        }

        @ParameterizedTest
        @ArgumentsSource(InvalidCollectionTypeParameterValueArgumentsProvider::class)
        fun givenInvalidParameterItShouldExecuteParameterInvalidValueHandler(lastParameterType: Class<*>) {
            val stringResolver = mockk<CommandParameterResolver<String>> {
                every { resolve("lol") } returns "lol"
                every { resolve("rofl") } returns "rofl"
                every { resolve("hahaha") } returns null
            }
            val parameter1 = mockk<CommandParameterDefinition> {
                every { resolver } returns stringResolver
                every { type } returns String::class.java
            }
            val invalidCommandParameterValueHandler = mockk<InvalidCommandParameterValueHandler> {
                every { handle(any(), any(), any(), any()) } returns OnPlayerCommandTextListener.Result.Processed
            }
            val parameter2 = mockk<CommandParameterDefinition> {
                every { resolver } returns stringResolver
                every { type } returns lastParameterType
                every { this@mockk.invalidCommandParameterValueHandler } returns invalidCommandParameterValueHandler
            }
            val stringParameterValues = listOf("lol", "rofl", "hahaha", "1234")
            val commandDefinition = CommandDefinition(
                    name = "foo",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf(parameter1, parameter2),
                    isGreedy = false
            )

            val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)

            assertThat(result)
                    .isInstanceOfSatisfying(CommandParametersResolver.Result.Error::class.java) {
                        assertThat(it.returnValue)
                                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
                    }
            verify {
                invalidCommandParameterValueHandler.handle(player, commandDefinition, stringParameterValues, 1)
            }
        }
    }

    private class ResolutionToListArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(Iterable::class.java),
                        Arguments.of(Collection::class.java),
                        Arguments.of(List::class.java)
                )

    }

    private class InvalidCollectionTypeParameterValueArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(Iterable::class.java),
                        Arguments.of(Collection::class.java),
                        Arguments.of(List::class.java),
                        Arguments.of(Set::class.java)
                )

    }

    private object TestCommands : Commands()

}
