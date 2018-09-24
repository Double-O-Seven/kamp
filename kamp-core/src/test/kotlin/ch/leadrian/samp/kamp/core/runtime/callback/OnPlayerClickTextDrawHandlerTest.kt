package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener.Result.NotFound
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener.Result.Processed
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnPlayerClickTextDrawHandlerTest {

    private val onPlayerClickTextDrawHandler = OnPlayerClickTextDrawHandler()
    private val player = mockk<Player>()
    private val textDraw = mockk<TextDraw>()

    @Test
    fun givenNoListenerItShouldReturnNotFoundResult() {
        val result = onPlayerClickTextDrawHandler.onPlayerClickTextDraw(player, textDraw)

        assertThat(result)
                .isEqualTo(NotFound)
    }

    @Test
    fun givenAllListenersReturnNotFoundItShouldReturnNotFound() {
        val listener1 = mockk<OnPlayerClickTextDrawListener> {
            every {
                onPlayerClickTextDraw(player, textDraw)
            } returns NotFound
        }
        val listener2 = mockk<OnPlayerClickTextDrawListener> {
            every {
                onPlayerClickTextDraw(player, textDraw)
            } returns NotFound
        }
        val listener3 = mockk<OnPlayerClickTextDrawListener> {
            every {
                onPlayerClickTextDraw(player, textDraw)
            } returns NotFound
        }
        val onPlayerClickTextDrawHandler = OnPlayerClickTextDrawHandler()
        onPlayerClickTextDrawHandler.register(listener1)
        onPlayerClickTextDrawHandler.register(listener2)
        onPlayerClickTextDrawHandler.register(listener3)

        val result = onPlayerClickTextDrawHandler.onPlayerClickTextDraw(player, textDraw)

        verify(exactly = 1) {
            listener1.onPlayerClickTextDraw(player, textDraw)
            listener2.onPlayerClickTextDraw(player, textDraw)
            listener3.onPlayerClickTextDraw(player, textDraw)
        }
        assertThat(result)
                .isEqualTo(NotFound)
    }

    @Test
    fun shouldStopWithFirstProcessedResult() {
        val listener1 = mockk<OnPlayerClickTextDrawListener> {
            every { onPlayerClickTextDraw(player, textDraw) } returns NotFound
        }
        val listener2 = mockk<OnPlayerClickTextDrawListener> {
            every { onPlayerClickTextDraw(player, textDraw) } returns Processed
        }
        val listener3 = mockk<OnPlayerClickTextDrawListener>()
        val onPlayerClickTextDrawHandler = OnPlayerClickTextDrawHandler()
        onPlayerClickTextDrawHandler.register(listener1)
        onPlayerClickTextDrawHandler.register(listener2)
        onPlayerClickTextDrawHandler.register(listener3)

        val result = onPlayerClickTextDrawHandler.onPlayerClickTextDraw(player, textDraw)

        verify(exactly = 1) {
            listener1.onPlayerClickTextDraw(player, textDraw)
            listener2.onPlayerClickTextDraw(player, textDraw)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Processed)
    }

}