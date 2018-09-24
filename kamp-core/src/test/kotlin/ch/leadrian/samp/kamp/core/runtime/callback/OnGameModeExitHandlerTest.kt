package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnGameModeExitListener
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnGameModeExitHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnGameModeExitListener>(relaxed = true)
        val listener2 = mockk<OnGameModeExitListener>(relaxed = true)
        val listener3 = mockk<OnGameModeExitListener>(relaxed = true)
        val onGameModeExitHandler = OnGameModeExitHandler()
        onGameModeExitHandler.register(listener1)
        onGameModeExitHandler.register(listener2)
        onGameModeExitHandler.register(listener3)

        onGameModeExitHandler.onGameModeExit()

        verify(exactly = 1) {
            listener1.onGameModeExit()
            listener2.onGameModeExit()
            listener3.onGameModeExit()
        }
    }

}