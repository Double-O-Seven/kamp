package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.Commands
import com.google.common.hash.HashCode
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CommandRegistryTest {

    private val commandDefinitionLoader = mockk<CommandDefinitionLoader>()
    private val method = HashCode::class.java.getMethod("hashCode")

    @Nested
    inner class ValidCommandDefinitionsTests {

        private lateinit var commandRegistry: CommandRegistry

        private val commandDefinitionWithoutAliases: CommandDefinition = CommandDefinition(
                name = "foo",
                commandsInstance = FooCommands,
                parameters = listOf(),
                method = method
        )
        private val commandDefinitionWithAliases = CommandDefinition(
                name = "bar",
                aliases = setOf("baralias1", "baralias2"),
                commandsInstance = BarCommands,
                parameters = listOf(),
                method = method
        )
        private val groupedCommandDefinitionWithoutAliases = CommandDefinition(
                name = "bla",
                groupName = "baz",
                commandsInstance = BazCommands,
                parameters = listOf(),
                method = method
        )
        private val groupedCommandDefinitionWithAliases = CommandDefinition(
                name = "blub",
                aliases = setOf("blubalias1", "blubalias2"),
                groupName = "baz",
                commandsInstance = BazCommands,
                parameters = listOf(),
                method = method
        )

        @BeforeEach
        fun setUp() {
            every { commandDefinitionLoader.load(FooCommands) } returns listOf(commandDefinitionWithoutAliases)
            every { commandDefinitionLoader.load(BarCommands) } returns listOf(commandDefinitionWithAliases)
            every { commandDefinitionLoader.load(BazCommands) } returns listOf(
                    groupedCommandDefinitionWithoutAliases,
                    groupedCommandDefinitionWithAliases
            )
            commandRegistry = CommandRegistry(setOf(FooCommands, BarCommands, BazCommands), commandDefinitionLoader)
        }

        @Test
        fun shouldInitialize() {
            val caughtThrowable = catchThrowable { commandRegistry.initialize() }

            assertThat(caughtThrowable)
                    .isNull()
        }

        @Test
        fun shouldReturnCommandDefinitionWithoutAliases() {
            commandRegistry.initialize()

            val commandDefinition = commandRegistry.getCommandDefinition("foo", null)

            assertThat(commandDefinition)
                    .isEqualTo(commandDefinitionWithoutAliases)
        }

        @ParameterizedTest
        @ValueSource(strings = ["bar", "baralias1", "baralias2"])
        fun shouldReturnCommandDefinitionWithAliases(commandName: String) {
            commandRegistry.initialize()

            val commandDefinition = commandRegistry.getCommandDefinition(commandName, null)

            assertThat(commandDefinition)
                    .isEqualTo(commandDefinitionWithAliases)
        }

        @Test
        fun shouldReturnGroupedCommandWithoutAliases() {
            commandRegistry.initialize()

            val commandDefinition = commandRegistry.getCommandDefinition("baz", "bla")

            assertThat(commandDefinition)
                    .isEqualTo(groupedCommandDefinitionWithoutAliases)
        }

        @ParameterizedTest
        @ValueSource(strings = ["blub", "blubalias1", "blubalias2"])
        fun shouldReturnGroupedCommandDefinitionWithAliases(commandName: String) {
            commandRegistry.initialize()

            val commandDefinition = commandRegistry.getCommandDefinition("baz", commandName)

            assertThat(commandDefinition)
                    .isEqualTo(groupedCommandDefinitionWithAliases)
        }

        @Test
        fun givenUnknownCommandItShouldReturnNull() {
            commandRegistry.initialize()

            val commandDefinition = commandRegistry.getCommandDefinition("hahaha", null)

            assertThat(commandDefinition)
                    .isNull()
        }

        @Test
        fun givenUnknownCommandInGroupItShouldReturnNull() {
            commandRegistry.initialize()

            val commandDefinition = commandRegistry.getCommandDefinition("baz", "hahaha")

            assertThat(commandDefinition)
                    .isNull()
        }
    }

    @Nested
    inner class InvalidCommandDefinitionsTests {

        @Test
        fun givenDuplicateSingleCommandsItShouldThrowException() {
            every { commandDefinitionLoader.load(FooCommands) } returns listOf(
                    CommandDefinition(
                            name = "foo",
                            commandsInstance = FooCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            every { commandDefinitionLoader.load(BarCommands) } returns listOf(
                    CommandDefinition(
                            name = "foo",
                            commandsInstance = BarCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            val commandRegistry = CommandRegistry(setOf(FooCommands, BarCommands), commandDefinitionLoader)

            val caughtThrowable = catchThrowable { commandRegistry.initialize() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Duplicate command or command group with name or alias foo")
        }

        @Test
        fun givenDuplicateSingleCommandsWithAliasItShouldThrowException() {
            every { commandDefinitionLoader.load(FooCommands) } returns listOf(
                    CommandDefinition(
                            name = "foo",
                            aliases = setOf("qux"),
                            commandsInstance = FooCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            every { commandDefinitionLoader.load(BarCommands) } returns listOf(
                    CommandDefinition(
                            name = "bar",
                            aliases = setOf("foo"),
                            commandsInstance = BarCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            val commandRegistry = CommandRegistry(setOf(FooCommands, BarCommands), commandDefinitionLoader)

            val caughtThrowable = catchThrowable { commandRegistry.initialize() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Duplicate command or command group with name or alias foo")
        }

        @Test
        fun givenSingleCommandIsAlreadyRegisteredAddingCommandGroupShouldThrowException() {
            every { commandDefinitionLoader.load(FooCommands) } returns listOf(
                    CommandDefinition(
                            name = "foo",
                            commandsInstance = FooCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            every { commandDefinitionLoader.load(BarCommands) } returns listOf(
                    CommandDefinition(
                            name = "bar",
                            groupName = "foo",
                            commandsInstance = BarCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            val commandRegistry = CommandRegistry(setOf(FooCommands, BarCommands), commandDefinitionLoader)

            val caughtThrowable = catchThrowable { commandRegistry.initialize() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Command and command group with same name: foo")
        }

        @Test
        fun givenCommandGroupIsAlreadyRegisteredAddingSingleCommandShouldThrowException() {
            every { commandDefinitionLoader.load(BarCommands) } returns listOf(
                    CommandDefinition(
                            name = "bar",
                            groupName = "foo",
                            commandsInstance = BarCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            every { commandDefinitionLoader.load(FooCommands) } returns listOf(
                    CommandDefinition(
                            name = "foo",
                            commandsInstance = FooCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            val commandRegistry = CommandRegistry(setOf(BarCommands, FooCommands), commandDefinitionLoader)

            val caughtThrowable = catchThrowable { commandRegistry.initialize() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Duplicate command or command group with name or alias foo")
        }

        @Test
        fun givenDuplicateCommandsWithinGroupItShouldThrowException() {
            every { commandDefinitionLoader.load(FooCommands) } returns listOf(
                    CommandDefinition(
                            name = "foo",
                            groupName = "bla",
                            commandsInstance = FooCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            every { commandDefinitionLoader.load(BarCommands) } returns listOf(
                    CommandDefinition(
                            name = "foo",
                            groupName = "bla",
                            commandsInstance = BarCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            val commandRegistry = CommandRegistry(setOf(FooCommands, BarCommands), commandDefinitionLoader)

            val caughtThrowable = catchThrowable { commandRegistry.initialize() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Duplicate command foo within group bla")
        }

        @Test
        fun givenDuplicateCommandsWithAliasesWithinGroupItShouldThrowException() {
            every { commandDefinitionLoader.load(FooCommands) } returns listOf(
                    CommandDefinition(
                            name = "foo",
                            aliases = setOf("qux"),
                            groupName = "bla",
                            commandsInstance = FooCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            every { commandDefinitionLoader.load(BarCommands) } returns listOf(
                    CommandDefinition(
                            name = "bar",
                            aliases = setOf("foo"),
                            groupName = "bla",
                            commandsInstance = BarCommands,
                            parameters = listOf(),
                            method = method
                    )
            )
            val commandRegistry = CommandRegistry(setOf(FooCommands, BarCommands), commandDefinitionLoader)

            val caughtThrowable = catchThrowable { commandRegistry.initialize() }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Duplicate command foo within group bla")
        }

    }

    private object FooCommands : Commands()

    private object BarCommands : Commands()

    private object BazCommands : Commands()

}