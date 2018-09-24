package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerPickUpPickupListener
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerPickUpPickupHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerPickUpPickupListener>(relaxed = true)
        val listener2 = mockk<OnPlayerPickUpPickupListener>(relaxed = true)
        val listener3 = mockk<OnPlayerPickUpPickupListener>(relaxed = true)
        val player = mockk<Player>()
        val pickup = mockk<Pickup>()
        val onPlayerPickUpPickupHandler = OnPlayerPickUpPickupHandler()
        onPlayerPickUpPickupHandler.register(listener1)
        onPlayerPickUpPickupHandler.register(listener2)
        onPlayerPickUpPickupHandler.register(listener3)

        onPlayerPickUpPickupHandler.onPlayerPickUpPickup(player, pickup)

        verify(exactly = 1) {
            listener1.onPlayerPickUpPickup(player, pickup)
            listener2.onPlayerPickUpPickup(player, pickup)
            listener3.onPlayerPickUpPickup(player, pickup)
        }
    }

}