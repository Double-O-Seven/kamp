package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandDescription
import ch.leadrian.samp.kamp.core.api.command.CommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.CommandParameterDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.Description
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CommandDefinitionLoaderTest {

    private lateinit var commandDefinitionLoader: CommandDefinitionLoader

    private lateinit var injector: Injector
    private val textProvider = mockk<TextProvider>()
    private val commandParameterResolverRegistry = mockk<CommandParameterResolverRegistry>()

    @BeforeEach
    fun setUp() {
        every { commandParameterResolverRegistry.getResolver(Int::class.javaPrimitiveType!!) } returns PrimitiveIntParameterResolver
        every { commandParameterResolverRegistry.getResolver(Int::class.javaObjectType) } returns IntParameterResolver
        every { commandParameterResolverRegistry.getResolver(String::class.java) } returns StringParameterResolver
        injector = Guice.createInjector(TestModule())
        commandDefinitionLoader = CommandDefinitionLoader(
                injector = injector,
                textProvider = textProvider,
                commandParameterResolverRegistry = commandParameterResolverRegistry
        )
    }

    @Test
    fun shouldLoadCommandParameterResolvers() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsForCommandParameterResolverTests::class.java)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "say",
                                method = CommandsForCommandParameterResolverTests::class.java.getMethod(
                                        "say",
                                        Player::class.java,
                                        String::class.java
                                ),
                                parameters = listOf(CommandParameterDefinition(
                                        type = String::class.java,
                                        resolver = StringParameterResolver,
                                        textProvider = textProvider
                                ))
                        ),
                        CommandDefinition(
                                name = "setname",
                                method = CommandsForCommandParameterResolverTests::class.java.getMethod(
                                        "setName",
                                        Player::class.java,
                                        Int::class.javaPrimitiveType,
                                        String::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = Int::class.javaPrimitiveType!!,
                                                resolver = PrimitiveIntParameterResolver,
                                                textProvider = textProvider
                                        ),
                                        CommandParameterDefinition(
                                                type = String::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "banall",
                                method = CommandsForCommandParameterResolverTests::class.java.getMethod(
                                        "banAll",
                                        Player::class.java,
                                        String::class.java,
                                        List::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = String::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        ),
                                        CommandParameterDefinition(
                                                type = List::class.javaObjectType,
                                                resolver = IntParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "kill",
                                method = CommandsForCommandParameterResolverTests::class.java.getMethod(
                                        "kill",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    class CommandsForCommandParameterResolverTests : Commands() {

        @Command
        fun say(player: Player, message: String) {
        }

        @Command
        fun setName(player: Player, playerId: Int, name: String) {
        }

        @Command
        fun banAll(player: Player, reason: String, playerIds: List<Int>) {
        }

        @Command
        fun kill(player: Player) {
        }

    }

    @Test
    fun shouldLoadDescriptions() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithDescriptions::class.java)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                description = CommandDescription(
                                        text = "Foo command",
                                        textKey = TextKey("command.foo.description"),
                                        textProvider = textProvider
                                ),
                                method = CommandsWithDescriptions::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "bar",
                                description = CommandDescription(
                                        textKey = TextKey("command.bar.description"),
                                        textProvider = textProvider
                                ),
                                method = CommandsWithDescriptions::class.java.getMethod(
                                        "bar",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "baz",
                                description = CommandDescription(
                                        text = "Baz command",
                                        textProvider = textProvider
                                ),
                                method = CommandsWithDescriptions::class.java.getMethod(
                                        "baz",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "lol",
                                method = CommandsWithDescriptions::class.java.getMethod(
                                        "lol",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    class CommandsWithDescriptions : Commands() {

        @Command
        @Description(text = "Foo command", textKey = "command.foo.description")
        fun foo(player: Player) {
        }

        @Command
        @Description(textKey = "command.bar.description")
        fun bar(player: Player) {
        }

        @Command
        @Description(text = "Baz command")
        fun baz(player: Player) {
        }

        @Command
        @Description
        fun lol(player: Player) {
        }

    }

    private class TestModule : AbstractModule() {

        override fun configure() {
            bind(FooCommandAccessChecker::class.java)
            bind(BarCommandAccessChecker::class.java)
            bind(FooCommandErrorHandler::class.java)
            bind(BarCommandErrorHandler::class.java)
        }

    }

    object StringParameterResolver : CommandParameterResolver<String> {

        override val parameterType: Class<String>
            get() = String::class.java

        override fun resolve(value: String): String = throw UnsupportedOperationException("test")

    }

    object IntParameterResolver : CommandParameterResolver<Int> {

        override val parameterType: Class<Int>
            get() = Int::class.javaObjectType

        override fun resolve(value: String): Int = throw UnsupportedOperationException("test")

    }

    object PrimitiveIntParameterResolver : CommandParameterResolver<Int> {

        override val parameterType: Class<Int>
            get() = Int::class.javaPrimitiveType!!

        override fun resolve(value: String): Int = throw UnsupportedOperationException("test")

    }

    class FooCommandAccessChecker : CommandAccessChecker {

        override fun isAccessGranted(player: Player, commandDefinition: CommandDefinition, parameters: List<String>): Boolean = true

    }

    class BarCommandAccessChecker : CommandAccessChecker {

        override fun isAccessGranted(player: Player, commandDefinition: CommandDefinition, parameters: List<String>): Boolean = true

    }

    class FooCommandErrorHandler : CommandErrorHandler {

        override fun handle(player: Player, commandLine: String): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

    class BarCommandErrorHandler : CommandErrorHandler {

        override fun handle(player: Player, commandLine: String): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

}