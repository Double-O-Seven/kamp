package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.CommandAccessCheckerGroup
import ch.leadrian.samp.kamp.core.api.command.CommandAccessDeniedHandler
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.DefaultCommandAccessDeniedHandler
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale

internal class CommandAccessCheckExecutorTest {

    private lateinit var commandAccessCheckExecutor: CommandAccessCheckExecutor

    private val locale = Locale.GERMANY
    private val messageSender = mockk<MessageSender>()
    private val defaultCommandAccessDeniedHandler = mockk<DefaultCommandAccessDeniedHandler>()
    private val method = Any::class.java.getMethod("hashCode")
    private val player = mockk<Player>()

    @BeforeEach
    fun setUp() {
        every { player.locale } returns locale
        commandAccessCheckExecutor = CommandAccessCheckExecutor(messageSender, defaultCommandAccessDeniedHandler)
    }

    @Test
    fun givenNoAccessCheckersItShouldReturnNull() {
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                parameters = listOf(),
                commandsInstance = TestCommands,
                accessCheckers = listOf()
        )

        val result = commandAccessCheckExecutor.checkAccess(player, commandDefinition, listOf())

        assertThat(result)
                .isNull()
    }

    @Test
    fun shouldOnlyExecuteFirstAccessCheckers() {
        val accessChecker1 = mockk<CommandAccessChecker> {
            every { hasAccess(any(), any(), any()) } returns true
        }
        val accessChecker2 = mockk<CommandAccessChecker> {
            every { hasAccess(any(), any(), any()) } returns false
        }
        val accessChecker3 = mockk<CommandAccessChecker>(relaxed = true)
        val accessCheckerGroup1 = mockk<CommandAccessCheckerGroup> {
            every { accessCheckers } returns listOf(accessChecker1)
            every { accessDeniedHandlers } returns listOf()
        }
        val accessCheckerGroup2 = mockk<CommandAccessCheckerGroup> {
            every { accessCheckers } returns listOf(accessChecker2, accessChecker3)
            every { getErrorMessage(any()) } returns null
            every { accessDeniedHandlers } returns listOf()
        }
        every {
            defaultCommandAccessDeniedHandler
                    .handle(any(), any(), any())
        } returns OnPlayerCommandTextListener.Result.Processed
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                parameters = listOf(),
                commandsInstance = TestCommands,
                accessCheckers = listOf(accessCheckerGroup1, accessCheckerGroup2)
        )
        val stringParameterValues = listOf("test")

        val result = commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)

        verify(exactly = 1) {
            accessChecker1.hasAccess(player, commandDefinition, stringParameterValues)
            accessChecker2.hasAccess(player, commandDefinition, stringParameterValues)
        }
        verify { accessChecker3 wasNot Called }
        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

    @Test
    fun givenNoAccessDeniedHandlersOrErrorMessageItShouldExecuteDefaultAccessDeniedHandler() {
        val accessChecker = mockk<CommandAccessChecker> {
            every { hasAccess(any(), any(), any()) } returns false
        }
        val accessCheckerGroup = mockk<CommandAccessCheckerGroup> {
            every { accessCheckers } returns listOf(accessChecker)
            every { getErrorMessage(any()) } returns null
            every { accessDeniedHandlers } returns listOf()
        }
        every {
            defaultCommandAccessDeniedHandler.handle(any(), any(), any())
        } returns OnPlayerCommandTextListener.Result.Processed
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                parameters = listOf(),
                commandsInstance = TestCommands,
                accessCheckers = listOf(accessCheckerGroup)
        )
        val stringParameterValues = listOf("test")

        val result = commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)

        verify {
            defaultCommandAccessDeniedHandler.handle(player, commandDefinition, stringParameterValues)
        }
        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

    @Test
    fun givenNoAccessDeniedHandlersOrErrorMessageItShouldExecuteInjectedAccessDeniedHandler() {
        val injectedDefaultAccessDeniedHandler = mockk<CommandAccessDeniedHandler> {
            every { handle(any(), any(), any()) } returns OnPlayerCommandTextListener.Result.Processed
        }
        val module = object : AbstractModule() {

            override fun configure() {
                bind(CommandAccessDeniedHandler::class.java).toInstance(injectedDefaultAccessDeniedHandler)
            }

        }
        val injector = Guice.createInjector(module)
        injector.injectMembers(commandAccessCheckExecutor)
        val accessChecker = mockk<CommandAccessChecker> {
            every { hasAccess(any(), any(), any()) } returns false
        }
        val accessCheckerGroup = mockk<CommandAccessCheckerGroup> {
            every { accessCheckers } returns listOf(accessChecker)
            every { getErrorMessage(any()) } returns null
            every { accessDeniedHandlers } returns listOf()
        }
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                parameters = listOf(),
                commandsInstance = TestCommands,
                accessCheckers = listOf(accessCheckerGroup)
        )
        val stringParameterValues = listOf("test")

        val result = commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)

        verify {
            injectedDefaultAccessDeniedHandler.handle(player, commandDefinition, stringParameterValues)
        }
        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

    @Test
    fun givenErrorMessageButNoAccessDeniedHandlersItShouldSendErrorMessage() {
        val accessChecker = mockk<CommandAccessChecker> {
            every { hasAccess(any(), any(), any()) } returns false
        }
        val accessCheckerGroup = mockk<CommandAccessCheckerGroup> {
            every { accessCheckers } returns listOf(accessChecker)
            every { getErrorMessage(any()) } returns "Shit happened"
            every { accessDeniedHandlers } returns listOf()
        }
        every { messageSender.sendMessageToPlayer(any(), any(), any<String>()) } just Runs
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                parameters = listOf(),
                commandsInstance = TestCommands,
                accessCheckers = listOf(accessCheckerGroup)
        )
        val stringParameterValues = listOf("test")

        val result = commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)

        verify {
            messageSender.sendMessageToPlayer(player, Colors.RED, "Shit happened")
            defaultCommandAccessDeniedHandler wasNot Called
        }
        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

    @Test
    fun givenAccessDeniedHandlerItShouldExecuteAccessDeniedHandler() {
        val accessChecker = mockk<CommandAccessChecker> {
            every { hasAccess(any(), any(), any()) } returns false
        }
        val accessDeniedHandler = mockk<CommandAccessDeniedHandler> {
            every { handle(any(), any(), any()) } returns OnPlayerCommandTextListener.Result.Processed
        }
        val accessCheckerGroup = mockk<CommandAccessCheckerGroup> {
            every { accessCheckers } returns listOf(accessChecker)
            every { getErrorMessage(any()) } returns "Shit happened"
            every { accessDeniedHandlers } returns listOf(accessDeniedHandler)
        }
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                parameters = listOf(),
                commandsInstance = TestCommands,
                accessCheckers = listOf(accessCheckerGroup)
        )
        val stringParameterValues = listOf("test")

        val result = commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)

        verify {
            accessDeniedHandler.handle(player, commandDefinition, stringParameterValues)
            messageSender wasNot Called
        }
        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

    @Test
    fun givenMultipleAccessDeniedHandlerItShouldReturnOnFirstProcessedResult() {
        val accessChecker = mockk<CommandAccessChecker> {
            every { hasAccess(any(), any(), any()) } returns false
        }
        val accessDeniedHandler1 = mockk<CommandAccessDeniedHandler> {
            every { handle(any(), any(), any()) } returns OnPlayerCommandTextListener.Result.UnknownCommand
        }
        val accessDeniedHandler2 = mockk<CommandAccessDeniedHandler> {
            every { handle(any(), any(), any()) } returns OnPlayerCommandTextListener.Result.Processed
        }
        val accessDeniedHandler3 = mockk<CommandAccessDeniedHandler> {
            every { handle(any(), any(), any()) } returns OnPlayerCommandTextListener.Result.Processed
        }
        val accessCheckerGroup = mockk<CommandAccessCheckerGroup> {
            every { accessCheckers } returns listOf(accessChecker)
            every { getErrorMessage(any()) } returns "Shit happened"
            every { accessDeniedHandlers } returns listOf(
                    accessDeniedHandler1,
                    accessDeniedHandler2,
                    accessDeniedHandler3
            )
        }
        val commandDefinition = CommandDefinition(
                name = "foo",
                method = method,
                parameters = listOf(),
                commandsInstance = TestCommands,
                accessCheckers = listOf(accessCheckerGroup)
        )
        val stringParameterValues = listOf("test")

        val result = commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)

        verify {
            accessDeniedHandler1.handle(player, commandDefinition, stringParameterValues)
            accessDeniedHandler2.handle(player, commandDefinition, stringParameterValues)
            accessDeniedHandler3 wasNot Called
        }
        assertThat(result)
                .isEqualTo(OnPlayerCommandTextListener.Result.Processed)
    }

    private object TestCommands : Commands()

}
