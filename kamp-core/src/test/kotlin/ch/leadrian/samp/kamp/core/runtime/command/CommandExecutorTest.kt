package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class CommandExecutorTest {

    private val commandExecutor = CommandExecutor()

    private val player = mockk<Player>()
    private val testService = mockk<TestService>(relaxed = true)

    @ParameterizedTest
    @ArgumentsSource(ExecuteArgumentsProvider::class)
    fun shouldExecuteCommand(commandMethodName: String, expectedResult: OnPlayerCommandTextListener.Result) {
        val commandDefinition = CommandDefinition(
                name = commandMethodName,
                parameters = listOf(),
                commandsInstance = TestCommands(testService),
                method = TestCommands::class.java.getMethod(commandMethodName, Player::class.java, Int::class.javaPrimitiveType)
        )
        val parameterValues = arrayOf(player, 1337)

        val result = commandExecutor.execute(commandDefinition, parameterValues)

        assertThat(result)
                .isEqualTo(expectedResult)
        verify { testService.test(player, commandMethodName, 1337) }
    }

    private class ExecuteArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<ExecuteArguments> =
                Stream.of(
                        ExecuteArguments("foo", OnPlayerCommandTextListener.Result.Processed),
                        ExecuteArguments("bar", OnPlayerCommandTextListener.Result.Processed),
                        ExecuteArguments("baz", OnPlayerCommandTextListener.Result.UnknownCommand),
                        ExecuteArguments("bat", OnPlayerCommandTextListener.Result.UnknownCommand),
                        ExecuteArguments("qux", OnPlayerCommandTextListener.Result.Processed),
                        ExecuteArguments("foobar", OnPlayerCommandTextListener.Result.UnknownCommand),
                        ExecuteArguments("bla", OnPlayerCommandTextListener.Result.Processed),
                        ExecuteArguments("blub", OnPlayerCommandTextListener.Result.UnknownCommand),
                        ExecuteArguments("lol", OnPlayerCommandTextListener.Result.Processed),
                        ExecuteArguments("rofl", OnPlayerCommandTextListener.Result.UnknownCommand)
                )

    }

    private class ExecuteArguments(
            private val commandMethodName: String,
            private val expectedResult: OnPlayerCommandTextListener.Result
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(commandMethodName, expectedResult)

    }

    @Suppress("UNUSED_PARAMETER")
    private class TestCommands(val testService: TestService) : Commands() {

        @Command
        fun foo(player: Player, value: Int) {
            testService.test(player, "foo", value)
        }

        @Command
        fun bar(player: Player, value: Int): Boolean {
            testService.test(player, "bar", value)
            return true
        }

        @Command
        fun baz(player: Player, value: Int): Boolean {
            testService.test(player, "baz", value)
            return false
        }

        @Command
        fun bat(player: Player, value: Int): Boolean? {
            testService.test(player, "bat", value)
            return null
        }

        @Command
        fun qux(player: Player, value: Int): Boolean? {
            testService.test(player, "qux", value)
            return true
        }

        @Command
        fun foobar(player: Player, value: Int): Boolean? {
            testService.test(player, "foobar", value)
            return false
        }

        @Command
        fun bla(player: Player, value: Int): OnPlayerCommandTextListener.Result {
            testService.test(player, "bla", value)
            return OnPlayerCommandTextListener.Result.Processed
        }

        @Command
        fun blub(player: Player, value: Int): OnPlayerCommandTextListener.Result {
            testService.test(player, "blub", value)
            return OnPlayerCommandTextListener.Result.UnknownCommand
        }

        @Command
        fun lol(player: Player, value: Int): OnPlayerCommandTextListener.Result.Processed {
            testService.test(player, "lol", value)
            return OnPlayerCommandTextListener.Result.Processed
        }

        @Command
        fun rofl(player: Player, value: Int): OnPlayerCommandTextListener.Result.UnknownCommand {
            testService.test(player, "rofl", value)
            return OnPlayerCommandTextListener.Result.UnknownCommand
        }
    }

    private interface TestService {

        fun test(player: Player, commandMethodName: String, someIntValue: Int)

    }

}