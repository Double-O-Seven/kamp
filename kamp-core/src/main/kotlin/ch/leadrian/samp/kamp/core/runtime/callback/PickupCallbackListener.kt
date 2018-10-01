package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerPickUpPickupListener
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PickupCallbackListener
@Inject
constructor(private val callbackListenerManager: CallbackListenerManager) : OnPlayerPickUpPickupListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerPickUpPickup(player: Player, pickup: Pickup) {
        pickup.onPickUp(player)
    }
}