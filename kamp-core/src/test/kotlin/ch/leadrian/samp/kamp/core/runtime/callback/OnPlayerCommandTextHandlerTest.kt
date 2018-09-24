package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnPlayerCommandTextHandlerTest {

    private val onPlayerCommandTextHandler = OnPlayerCommandTextHandler()
    private val player = mockk<Player>()
    private val commandText = "/hi there"

    @Test
    fun givenNoListenerItShouldReturnUnknownCommandResult() {
        val result = onPlayerCommandTextHandler.onPlayerCommandText(player, commandText)

        assertThat(result)
                .isEqualTo(Result.UnknownCommand)
    }

    @Test
    fun givenAllListenersReturnUnknownCommandItShouldReturnUnknownCommand() {
        val listener1 = mockk<OnPlayerCommandTextListener> {
            every {
                onPlayerCommandText(player, commandText)
            } returns Result.UnknownCommand
        }
        val listener2 = mockk<OnPlayerCommandTextListener> {
            every {
                onPlayerCommandText(player, commandText)
            } returns Result.UnknownCommand
        }
        val listener3 = mockk<OnPlayerCommandTextListener> {
            every {
                onPlayerCommandText(player, commandText)
            } returns Result.UnknownCommand
        }
        val onPlayerCommandTextHandler = OnPlayerCommandTextHandler()
        onPlayerCommandTextHandler.register(listener1)
        onPlayerCommandTextHandler.register(listener2)
        onPlayerCommandTextHandler.register(listener3)

        val result = onPlayerCommandTextHandler.onPlayerCommandText(player, commandText)

        verify(exactly = 1) {
            listener1.onPlayerCommandText(player, commandText)
            listener2.onPlayerCommandText(player, commandText)
            listener3.onPlayerCommandText(player, commandText)
        }
        assertThat(result)
                .isEqualTo(Result.UnknownCommand)
    }

    @Test
    fun shouldStopWithFirstProcessedResult() {
        val listener1 = mockk<OnPlayerCommandTextListener> {
            every { onPlayerCommandText(player, commandText) } returns Result.UnknownCommand
        }
        val listener2 = mockk<OnPlayerCommandTextListener> {
            every { onPlayerCommandText(player, commandText) } returns Result.Processed
        }
        val listener3 = mockk<OnPlayerCommandTextListener>()
        val onPlayerCommandTextHandler = OnPlayerCommandTextHandler()
        onPlayerCommandTextHandler.register(listener1)
        onPlayerCommandTextHandler.register(listener2)
        onPlayerCommandTextHandler.register(listener3)

        val result = onPlayerCommandTextHandler.onPlayerCommandText(player, commandText)

        verify(exactly = 1) {
            listener1.onPlayerCommandText(player, commandText)
            listener2.onPlayerCommandText(player, commandText)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Processed)
    }

}