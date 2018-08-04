package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnTrailerUpdateListener {

    fun onTrailerUpdate(player: Player, vehicle: Vehicle): Boolean

}
