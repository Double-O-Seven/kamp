package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TextDrawCallbackListenerTest {

    private lateinit var textDrawCallbackListener: TextDrawCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        textDrawCallbackListener = TextDrawCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        textDrawCallbackListener.initialize()

        verify { callbackListenerManager.register(textDrawCallbackListener) }
    }

    @Nested
    inner class OnPlayerClickTextDrawTests {

        private val textDraw = mockk<TextDraw>()
        private val player = mockk<Player>()

        @Test
        fun givenOnClickReturnsProcessedItShouldReturnProcessed() {
            every { textDraw.onClick(player) } returns OnPlayerClickTextDrawListener.Result.Processed

            val result = textDrawCallbackListener.onPlayerClickTextDraw(player, textDraw)

            assertThat(result)
                    .isEqualTo(OnPlayerClickTextDrawListener.Result.Processed)
        }

        @Test
        fun givenOnClickReturnsNotFoundItShouldReturnNotFound() {
            every { textDraw.onClick(player) } returns OnPlayerClickTextDrawListener.Result.NotFound

            val result = textDrawCallbackListener.onPlayerClickTextDraw(player, textDraw)

            assertThat(result)
                    .isEqualTo(OnPlayerClickTextDrawListener.Result.NotFound)
        }

        @Test
        fun givenNullReferenceItShouldReturnNotFound() {
            every { textDraw.onClick(player) } returns OnPlayerClickTextDrawListener.Result.NotFound

            val result = textDrawCallbackListener.onPlayerClickTextDraw(player, null)

            assertThat(result)
                    .isEqualTo(OnPlayerClickTextDrawListener.Result.NotFound)
        }
    }

}