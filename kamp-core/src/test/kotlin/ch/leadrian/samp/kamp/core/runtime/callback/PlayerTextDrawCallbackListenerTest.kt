package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerTextDrawCallbackListenerTest {

    private lateinit var playerTextDrawCallbackListener: PlayerTextDrawCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        playerTextDrawCallbackListener = PlayerTextDrawCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        playerTextDrawCallbackListener.initialize()

        verify { callbackListenerManager.register(playerTextDrawCallbackListener) }
    }

    @Nested
    inner class OnPlayerClickPlayerTextDrawTests {

        private val playerTextDraw = mockk<PlayerTextDraw>()

        @Test
        fun givenOnClickReturnsProcessedItShouldReturnProcessed() {
            every { playerTextDraw.onClick() } returns OnPlayerClickPlayerTextDrawListener.Result.Processed

            val result = playerTextDrawCallbackListener.onPlayerClickPlayerTextDraw(playerTextDraw)

            assertThat(result)
                    .isEqualTo(OnPlayerClickPlayerTextDrawListener.Result.Processed)
        }

        @Test
        fun givenOnClickReturnsNotFoundItShouldReturnNotFound() {
            every { playerTextDraw.onClick() } returns OnPlayerClickPlayerTextDrawListener.Result.NotFound

            val result = playerTextDrawCallbackListener.onPlayerClickPlayerTextDraw(playerTextDraw)

            assertThat(result)
                    .isEqualTo(OnPlayerClickPlayerTextDrawListener.Result.NotFound)
        }
    }

}