package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnRconCommandListener
import ch.leadrian.samp.kamp.core.api.callback.OnRconCommandListener.Result
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnRconCommandHandlerTest {

    private val onRconCommandHandler = OnRconCommandHandler()
    private val command = "/hi there"

    @Test
    fun givenNoListenerItShouldReturnUnknownCommandResult() {
        val result = onRconCommandHandler.onRconCommand(command)

        assertThat(result)
                .isEqualTo(Result.UnknownCommand)
    }

    @Test
    fun givenAllListenersReturnUnknownCommandItShouldReturnUnknownCommand() {
        val listener1 = mockk<OnRconCommandListener> {
            every {
                onRconCommand(command)
            } returns Result.UnknownCommand
        }
        val listener2 = mockk<OnRconCommandListener> {
            every {
                onRconCommand(command)
            } returns Result.UnknownCommand
        }
        val listener3 = mockk<OnRconCommandListener> {
            every {
                onRconCommand(command)
            } returns Result.UnknownCommand
        }
        val onRconCommandHandler = OnRconCommandHandler()
        onRconCommandHandler.register(listener1)
        onRconCommandHandler.register(listener2)
        onRconCommandHandler.register(listener3)

        val result = onRconCommandHandler.onRconCommand(command)

        verify(exactly = 1) {
            listener1.onRconCommand(command)
            listener2.onRconCommand(command)
            listener3.onRconCommand(command)
        }
        assertThat(result)
                .isEqualTo(Result.UnknownCommand)
    }

    @Test
    fun shouldStopWithFirstProcessedResult() {
        val listener1 = mockk<OnRconCommandListener> {
            every { onRconCommand(command) } returns Result.UnknownCommand
        }
        val listener2 = mockk<OnRconCommandListener> {
            every { onRconCommand(command) } returns Result.Processed
        }
        val listener3 = mockk<OnRconCommandListener>()
        val onRconCommandHandler = OnRconCommandHandler()
        onRconCommandHandler.register(listener1)
        onRconCommandHandler.register(listener2)
        onRconCommandHandler.register(listener3)

        val result = onRconCommandHandler.onRconCommand(command)

        verify(exactly = 1) {
            listener1.onRconCommand(command)
            listener2.onRconCommand(command)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Processed)
    }

}