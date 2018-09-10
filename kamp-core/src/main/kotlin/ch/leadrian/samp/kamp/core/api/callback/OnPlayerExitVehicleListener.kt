package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnPlayerExitVehicleListener {

    fun onPlayerExitVehicle(player: Player, vehicle: Vehicle)

}
