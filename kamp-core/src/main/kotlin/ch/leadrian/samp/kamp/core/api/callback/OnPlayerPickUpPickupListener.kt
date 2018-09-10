package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerPickUpPickupListener {

    fun onPlayerPickUpPickup(player: Player, pickup: Pickup)

}
