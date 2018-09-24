package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener.Result.NotFound
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener.Result.Processed
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnPlayerClickPlayerTextDrawHandlerTest {

    private val onPlayerClickPlayerTextDrawHandler = OnPlayerClickPlayerTextDrawHandler()
    private val playerTextDraw = mockk<PlayerTextDraw>()

    @Test
    fun givenNoListenerItShouldReturnNotFoundResult() {
        val result = onPlayerClickPlayerTextDrawHandler.onPlayerClickPlayerTextDraw(playerTextDraw)

        assertThat(result)
                .isEqualTo(NotFound)
    }

    @Test
    fun givenAllListenersReturnNotFoundItShouldReturnNotFound() {
        val listener1 = mockk<OnPlayerClickPlayerTextDrawListener> {
            every {
                onPlayerClickPlayerTextDraw(playerTextDraw)
            } returns NotFound
        }
        val listener2 = mockk<OnPlayerClickPlayerTextDrawListener> {
            every {
                onPlayerClickPlayerTextDraw(playerTextDraw)
            } returns NotFound
        }
        val listener3 = mockk<OnPlayerClickPlayerTextDrawListener> {
            every {
                onPlayerClickPlayerTextDraw(playerTextDraw)
            } returns NotFound
        }
        val onPlayerClickPlayerTextDrawHandler = OnPlayerClickPlayerTextDrawHandler()
        onPlayerClickPlayerTextDrawHandler.register(listener1)
        onPlayerClickPlayerTextDrawHandler.register(listener2)
        onPlayerClickPlayerTextDrawHandler.register(listener3)

        val result = onPlayerClickPlayerTextDrawHandler.onPlayerClickPlayerTextDraw(playerTextDraw)

        verify(exactly = 1) {
            listener1.onPlayerClickPlayerTextDraw(playerTextDraw)
            listener2.onPlayerClickPlayerTextDraw(playerTextDraw)
            listener3.onPlayerClickPlayerTextDraw(playerTextDraw)
        }
        assertThat(result)
                .isEqualTo(NotFound)
    }

    @Test
    fun shouldStopWithFirstProcessedResult() {
        val listener1 = mockk<OnPlayerClickPlayerTextDrawListener> {
            every { onPlayerClickPlayerTextDraw(playerTextDraw) } returns NotFound
        }
        val listener2 = mockk<OnPlayerClickPlayerTextDrawListener> {
            every { onPlayerClickPlayerTextDraw(playerTextDraw) } returns Processed
        }
        val listener3 = mockk<OnPlayerClickPlayerTextDrawListener>()
        val onPlayerClickPlayerTextDrawHandler = OnPlayerClickPlayerTextDrawHandler()
        onPlayerClickPlayerTextDrawHandler.register(listener1)
        onPlayerClickPlayerTextDrawHandler.register(listener2)
        onPlayerClickPlayerTextDrawHandler.register(listener3)

        val result = onPlayerClickPlayerTextDrawHandler.onPlayerClickPlayerTextDraw(playerTextDraw)

        verify(exactly = 1) {
            listener1.onPlayerClickPlayerTextDraw(playerTextDraw)
            listener2.onPlayerClickPlayerTextDraw(playerTextDraw)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Processed)
    }

}