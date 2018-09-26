package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.DefaultCommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultUnknownCommandHandler
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CommandProcessorTest {

    private lateinit var commandProcessor: CommandProcessor

    private val commandParser = mockk<CommandParser>()
    private val commandRegistry = mockk<CommandRegistry>()
    private val commandAccessCheckExecutor = mockk<CommandAccessCheckExecutor>()
    private val commandParametersResolver = mockk<CommandParametersResolver>()
    private val commandExecutor = mockk<CommandExecutor>()
    private val unknownCommandHandler = mockk<DefaultUnknownCommandHandler>()
    private val defaultCommandErrorHandler = mockk<DefaultCommandErrorHandler>()
    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val player = mockk<Player>()
    private val method = Any::class.java.getMethod("hashCode")

    @BeforeEach
    fun setUp() {
        commandProcessor = CommandProcessor(
                commandParser = commandParser,
                commandRegistry = commandRegistry,
                commandAccessCheckExecutor = commandAccessCheckExecutor,
                commandParametersResolver = commandParametersResolver,
                commandExecutor = commandExecutor,
                defaultUnknownCommandHandler = unknownCommandHandler,
                defaultCommandErrorHandler = defaultCommandErrorHandler,
                callbackListenerManager = callbackListenerManager
        )
    }

    @Test
    fun initializeShouldRegisterAsCallbackListener() {
        every { callbackListenerManager.register(any()) } just Runs

        commandProcessor.initialize()

        verify { callbackListenerManager.register(commandProcessor) }
    }

    @Test
    fun givenCommandCouldBeParsedItShouldExecuteCommandErrorHandler() {
        every {
            defaultCommandErrorHandler.handle(any(), any(), any())
        } returns OnPlayerCommandTextListener.Result.Processed
        val commandLine = "/hi there"
        every { commandParser.parse(commandLine) } returns null

        commandProcessor.onPlayerCommandText(player, commandLine)

        verify {
            defaultCommandErrorHandler.handle(player, commandLine, null)
        }
    }

    @Test
    fun givenCommandDefinitionCouldNotBeFoundItShouldExecuteUnknownCommandHandler() {
        val commandLine = "/hi there"
        every { commandParser.parse(commandLine) } returns ParsedCommand("hi", listOf("there", "lol"))
        every { commandRegistry.getCommandDefinition("hi", "there") } returns null
        every {
            unknownCommandHandler.handle(any(), any(), any())
        } returns OnPlayerCommandTextListener.Result.Processed

        commandProcessor.onPlayerCommandText(player, commandLine)

        verify { unknownCommandHandler.handle(player, "hi", listOf("there", "lol")) }
    }

    @Nested
    inner class CommandWithGroupTests {

        @Test
        fun givenAccessDenialHasResultProcessedItShouldReturn() {
            val commandLine = "/hi there"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    groupName = "there",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", listOf("there", "lol"))
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, listOf("lol"))
            } returns OnPlayerCommandTextListener.Result.Processed

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
        }

        @Test
        fun givenAccessDenialHasResultUnknownCommandItShouldCallUnknownCommandHandler() {
            val commandLine = "/hi there lol"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    groupName = "there",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", listOf("there", "lol"))
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every { unknownCommandHandler.handle(any(), any(), any()) } returns OnPlayerCommandTextListener.Result.Processed
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, listOf("lol"))
            } returns OnPlayerCommandTextListener.Result.UnknownCommand

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            verify {
                unknownCommandHandler.handle(player, "hi", listOf("lol"))
            }
            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
        }

        @Test
        fun givenParametersAreResolvedItShouldExecuteCommands() {
            val commandLine = "/hi there"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    groupName = "there",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", listOf("there", "123"))
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, listOf("123"))
            } returns null
            every {
                commandParametersResolver.resolve(player, commandDefinition, listOf("123"))
            } returns CommandParametersResolver.Result.ParameterValues(arrayOf(player, 123))
            every { commandExecutor.execute(any(), any()) } returns OnPlayerCommandTextListener.Result.Processed

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
            verify {
                commandExecutor.execute(commandDefinition, arrayOf(player, 123))
            }
        }

        @Test
        fun givenParametersAreNotResolvedItShouldNotExecuteCommands() {
            val commandLine = "/hi there"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    groupName = "there",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", listOf("there", "123"))
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, listOf("123"))
            } returns null
            every {
                commandParametersResolver.resolve(player, commandDefinition, listOf("123"))
            } returns CommandParametersResolver.Result.Error(OnPlayerCommandTextListener.Result.Processed)
            every { commandExecutor.execute(any(), any()) } returns OnPlayerCommandTextListener.Result.Processed

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
            verify {
                commandExecutor wasNot Called
            }
        }

        @Test
        fun givenExceptionIsThrownItShouldExecutorCommandErrorHandler() {
            every { player.name } returns "hans.wurst"
            val commandLine = "/hi there"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    groupName = "there",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", listOf("there", "123"))
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, listOf("123"))
            } returns null
            every {
                commandParametersResolver.resolve(player, commandDefinition, listOf("123"))
            } returns CommandParametersResolver.Result.ParameterValues(arrayOf(player, 123))
            val exception = Exception("shit happened")
            every { commandExecutor.execute(any(), any()) } throws exception
            every {
                defaultCommandErrorHandler.handle(any(), any(), any())
            } returns OnPlayerCommandTextListener.Result.Processed

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
            verify { defaultCommandErrorHandler.handle(player, commandLine, exception) }
        }

    }

    @Nested
    inner class CommandWithoutGroupTests {

        @Test
        fun givenAccessIsDeniedItShouldReturn() {
            val commandLine = "/hi there"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", listOf("there", "lol"))
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, listOf("there", "lol"))
            } returns OnPlayerCommandTextListener.Result.Processed

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
        }

        @Test
        fun givenParametersAreResolvedItShouldExecuteCommands() {
            val commandLine = "/hi there"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            val stringParameterValues = listOf("there", "123")
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", stringParameterValues)
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)
            } returns null
            val parameterValues = arrayOf(player, "there", 123)
            every {
                commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)
            } returns CommandParametersResolver.Result.ParameterValues(parameterValues)
            every { commandExecutor.execute(any(), any()) } returns OnPlayerCommandTextListener.Result.Processed

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
            verify {
                commandExecutor.execute(commandDefinition, parameterValues)
            }
        }

        @Test
        fun givenParametersAreNotResolvedItShouldNotExecuteCommands() {
            val commandLine = "/hi there"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            val stringParameterValues = listOf("there", "123")
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", stringParameterValues)
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)
            } returns null
            every {
                commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)
            } returns CommandParametersResolver.Result.Error(OnPlayerCommandTextListener.Result.Processed)
            every { commandExecutor.execute(any(), any()) } returns OnPlayerCommandTextListener.Result.Processed

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
            verify {
                commandExecutor wasNot Called
            }
        }

        @Test
        fun givenExceptionIsThrownItShouldExecutorCommandErrorHandler() {
            every { player.name } returns "hans.wurst"
            val commandLine = "/hi there"
            val commandDefinition = CommandDefinition(
                    name = "hi",
                    method = method,
                    commandsInstance = TestCommands,
                    parameters = listOf()
            )
            val stringParameterValues = listOf("there", "123")
            every { commandParser.parse(commandLine) } returns ParsedCommand("hi", stringParameterValues)
            every { commandRegistry.getCommandDefinition("hi", "there") } returns commandDefinition
            every {
                commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)
            } returns null
            every {
                commandParametersResolver.resolve(player, commandDefinition, listOf("there", "123"))
            } returns CommandParametersResolver.Result.ParameterValues(arrayOf(player, "there", 123))
            val exception = Exception("shit happened")
            every { commandExecutor.execute(any(), any()) } throws exception
            every {
                defaultCommandErrorHandler.handle(any(), any(), any())
            } returns OnPlayerCommandTextListener.Result.Processed

            val result = commandProcessor.onPlayerCommandText(player, commandLine)

            assertThat(result)
                    .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
            verify { defaultCommandErrorHandler.handle(player, commandLine, exception) }
        }

    }

    private object TestCommands : Commands()
}