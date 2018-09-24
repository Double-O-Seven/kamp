package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTextListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTextListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnPlayerTextHandlerTest {

    private val onPlayerTextHandler = OnPlayerTextHandler()
    private val player = mockk<Player>()
    private val text = "/hi there"

    @Test
    fun givenNoListenerItShouldReturnAllowedResult() {
        val result = onPlayerTextHandler.onPlayerText(player, text)

        assertThat(result)
                .isEqualTo(Result.Allowed)
    }

    @Test
    fun givenAllListenersReturnAllowedItShouldReturnAllowed() {
        val listener1 = mockk<OnPlayerTextListener> {
            every {
                onPlayerText(player, text)
            } returns Result.Allowed
        }
        val listener2 = mockk<OnPlayerTextListener> {
            every {
                onPlayerText(player, text)
            } returns Result.Allowed
        }
        val listener3 = mockk<OnPlayerTextListener> {
            every {
                onPlayerText(player, text)
            } returns Result.Allowed
        }
        val onPlayerTextHandler = OnPlayerTextHandler()
        onPlayerTextHandler.register(listener1)
        onPlayerTextHandler.register(listener2)
        onPlayerTextHandler.register(listener3)

        val result = onPlayerTextHandler.onPlayerText(player, text)

        verify(exactly = 1) {
            listener1.onPlayerText(player, text)
            listener2.onPlayerText(player, text)
            listener3.onPlayerText(player, text)
        }
        assertThat(result)
                .isEqualTo(Result.Allowed)
    }

    @Test
    fun shouldStopWithFirstBlockedResult() {
        val listener1 = mockk<OnPlayerTextListener> {
            every { onPlayerText(player, text) } returns Result.Allowed
        }
        val listener2 = mockk<OnPlayerTextListener> {
            every { onPlayerText(player, text) } returns Result.Blocked
        }
        val listener3 = mockk<OnPlayerTextListener>()
        val onPlayerTextHandler = OnPlayerTextHandler()
        onPlayerTextHandler.register(listener1)
        onPlayerTextHandler.register(listener2)
        onPlayerTextHandler.register(listener3)

        val result = onPlayerTextHandler.onPlayerText(player, text)

        verify(exactly = 1) {
            listener1.onPlayerText(player, text)
            listener2.onPlayerText(player, text)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Blocked)
    }

}