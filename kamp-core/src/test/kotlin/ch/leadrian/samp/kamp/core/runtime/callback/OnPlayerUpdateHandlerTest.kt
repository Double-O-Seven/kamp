package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerUpdateListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnPlayerUpdateHandlerTest {

    private val onPlayerUpdateHandler = OnPlayerUpdateHandler()
    private val player = mockk<Player>()

    @Test
    fun givenNoListenerItShouldReturnSyncResult() {
        val result = onPlayerUpdateHandler.onPlayerUpdate(player)

        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun givenAllListenersReturnSyncItShouldReturnSync() {
        val listener1 = mockk<OnPlayerUpdateListener> {
            every {
                onPlayerUpdate(player)
            } returns Result.Sync
        }
        val listener2 = mockk<OnPlayerUpdateListener> {
            every {
                onPlayerUpdate(player)
            } returns Result.Sync
        }
        val listener3 = mockk<OnPlayerUpdateListener> {
            every {
                onPlayerUpdate(player)
            } returns Result.Sync
        }
        val onPlayerUpdateHandler = OnPlayerUpdateHandler()
        onPlayerUpdateHandler.register(listener1)
        onPlayerUpdateHandler.register(listener2)
        onPlayerUpdateHandler.register(listener3)

        val result = onPlayerUpdateHandler.onPlayerUpdate(player)

        verify(exactly = 1) {
            listener1.onPlayerUpdate(player)
            listener2.onPlayerUpdate(player)
            listener3.onPlayerUpdate(player)
        }
        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun shouldStopWithFirstDesyncResult() {
        val listener1 = mockk<OnPlayerUpdateListener> {
            every { onPlayerUpdate(player) } returns Result.Sync
        }
        val listener2 = mockk<OnPlayerUpdateListener> {
            every { onPlayerUpdate(player) } returns Result.Desync
        }
        val listener3 = mockk<OnPlayerUpdateListener>()
        val onPlayerUpdateHandler = OnPlayerUpdateHandler()
        onPlayerUpdateHandler.register(listener1)
        onPlayerUpdateHandler.register(listener2)
        onPlayerUpdateHandler.register(listener3)

        val result = onPlayerUpdateHandler.onPlayerUpdate(player)

        verify(exactly = 1) {
            listener1.onPlayerUpdate(player)
            listener2.onPlayerUpdate(player)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Desync)
    }

}