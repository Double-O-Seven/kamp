package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnPlayerExitVehicleListener {

    fun onPlayerExitVehicle(player: Player, vehicle: Vehicle)

}
