package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Pickup
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerPickUpPickupListener {

    fun onPlayerPickUpPickup(player: Player, pickup: Pickup)

}
