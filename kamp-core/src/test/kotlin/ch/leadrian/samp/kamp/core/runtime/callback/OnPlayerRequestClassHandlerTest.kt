package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestClassListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerRequestClassHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerRequestClassListener>(relaxed = true)
        val listener2 = mockk<OnPlayerRequestClassListener>(relaxed = true)
        val listener3 = mockk<OnPlayerRequestClassListener>(relaxed = true)
        val player = mockk<Player>()
        val playerClass = mockk<PlayerClass>()
        val onPlayerRequestClassHandler = OnPlayerRequestClassHandler()
        onPlayerRequestClassHandler.register(listener1)
        onPlayerRequestClassHandler.register(listener2)
        onPlayerRequestClassHandler.register(listener3)

        onPlayerRequestClassHandler.onPlayerRequestClass(player, playerClass)

        verify(exactly = 1) {
            listener1.onPlayerRequestClass(player, playerClass)
            listener2.onPlayerRequestClass(player, playerClass)
            listener3.onPlayerRequestClass(player, playerClass)
        }
    }

}