package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnGameModeInitListener
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnGameModeInitHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnGameModeInitListener>(relaxed = true)
        val listener2 = mockk<OnGameModeInitListener>(relaxed = true)
        val listener3 = mockk<OnGameModeInitListener>(relaxed = true)
        val onGameModeInitHandler = OnGameModeInitHandler()
        onGameModeInitHandler.register(listener1)
        onGameModeInitHandler.register(listener2)
        onGameModeInitHandler.register(listener3)

        onGameModeInitHandler.onGameModeInit()

        verify(exactly = 1) {
            listener1.onGameModeInit()
            listener2.onGameModeInit()
            listener3.onGameModeInit()
        }
    }

}