package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerPickUpPickupListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerPickUpPickupListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerPickUpPickupListener>(OnPlayerPickUpPickupListener::class), OnPlayerPickUpPickupListener {

    override fun onPlayerPickUpPickup(player: ch.leadrian.samp.kamp.core.api.entity.Player, pickup: ch.leadrian.samp.kamp.core.api.entity.Pickup) {
        getListeners().forEach {
            it.onPlayerPickUpPickup(player, pickup)
        }
    }

}
