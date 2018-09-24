package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitedMenuListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerExitedMenuHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerExitedMenuListener>(relaxed = true)
        val listener2 = mockk<OnPlayerExitedMenuListener>(relaxed = true)
        val listener3 = mockk<OnPlayerExitedMenuListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerExitedMenuHandler = OnPlayerExitedMenuHandler()
        onPlayerExitedMenuHandler.register(listener1)
        onPlayerExitedMenuHandler.register(listener2)
        onPlayerExitedMenuHandler.register(listener3)

        onPlayerExitedMenuHandler.onPlayerExitedMenu(player)

        verify(exactly = 1) {
            listener1.onPlayerExitedMenu(player)
            listener2.onPlayerExitedMenu(player)
            listener3.onPlayerExitedMenu(player)
        }
    }

}