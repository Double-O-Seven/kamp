package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerInteriorChangeListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerInteriorChangeHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerInteriorChangeListener>(relaxed = true)
        val listener2 = mockk<OnPlayerInteriorChangeListener>(relaxed = true)
        val listener3 = mockk<OnPlayerInteriorChangeListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerInteriorChangeHandler = OnPlayerInteriorChangeHandler()
        onPlayerInteriorChangeHandler.register(listener1)
        onPlayerInteriorChangeHandler.register(listener2)
        onPlayerInteriorChangeHandler.register(listener3)

        onPlayerInteriorChangeHandler.onPlayerInteriorChange(player = player, newInteriorId = 13, oldInteriorId = 69)

        verify(exactly = 1) {
            listener1.onPlayerInteriorChange(player = player, newInteriorId = 13, oldInteriorId = 69)
            listener2.onPlayerInteriorChange(player = player, newInteriorId = 13, oldInteriorId = 69)
            listener3.onPlayerInteriorChange(player = player, newInteriorId = 13, oldInteriorId = 69)
        }
    }

}