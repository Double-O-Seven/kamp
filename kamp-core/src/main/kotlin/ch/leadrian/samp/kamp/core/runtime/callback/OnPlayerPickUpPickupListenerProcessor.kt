package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerPickUpPickupListener
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerPickUpPickupListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerPickUpPickupListener>(OnPlayerPickUpPickupListener::class), OnPlayerPickUpPickupListener {

    override fun onPlayerPickUpPickup(player: Player, pickup: Pickup) {
        listeners.forEach {
            it.onPlayerPickUpPickup(player, pickup)
        }
    }

}
