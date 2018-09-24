package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnTrailerUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnTrailerUpdateListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnTrailerUpdateHandlerTest {

    private val onTrailerUpdateHandler = OnTrailerUpdateHandler()
    private val player = mockk<Player>()
    private val trailer = mockk<Vehicle>()

    @Test
    fun givenNoListenerItShouldReturnSyncResult() {
        val result = onTrailerUpdateHandler.onTrailerUpdate(player, trailer)

        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun givenAllListenersReturnSyncItShouldReturnSync() {
        val listener1 = mockk<OnTrailerUpdateListener> {
            every {
                onTrailerUpdate(player, trailer)
            } returns Result.Sync
        }
        val listener2 = mockk<OnTrailerUpdateListener> {
            every {
                onTrailerUpdate(player, trailer)
            } returns Result.Sync
        }
        val listener3 = mockk<OnTrailerUpdateListener> {
            every {
                onTrailerUpdate(player, trailer)
            } returns Result.Sync
        }
        val onTrailerUpdateHandler = OnTrailerUpdateHandler()
        onTrailerUpdateHandler.register(listener1)
        onTrailerUpdateHandler.register(listener2)
        onTrailerUpdateHandler.register(listener3)

        val result = onTrailerUpdateHandler.onTrailerUpdate(player, trailer)

        verify(exactly = 1) {
            listener1.onTrailerUpdate(player, trailer)
            listener2.onTrailerUpdate(player, trailer)
            listener3.onTrailerUpdate(player, trailer)
        }
        assertThat(result)
                .isEqualTo(Result.Sync)
    }

    @Test
    fun shouldStopWithFirstDesyncResult() {
        val listener1 = mockk<OnTrailerUpdateListener> {
            every { onTrailerUpdate(player, trailer) } returns Result.Sync
        }
        val listener2 = mockk<OnTrailerUpdateListener> {
            every { onTrailerUpdate(player, trailer) } returns Result.Desync
        }
        val listener3 = mockk<OnTrailerUpdateListener>()
        val onTrailerUpdateHandler = OnTrailerUpdateHandler()
        onTrailerUpdateHandler.register(listener1)
        onTrailerUpdateHandler.register(listener2)
        onTrailerUpdateHandler.register(listener3)

        val result = onTrailerUpdateHandler.onTrailerUpdate(player, trailer)

        verify(exactly = 1) {
            listener1.onTrailerUpdate(player, trailer)
            listener2.onTrailerUpdate(player, trailer)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Desync)
    }

}