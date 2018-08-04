package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnVehicleSirenStateChangeListener {

    fun onVehicleSirenStateChange(player: Player, vehicle: Vehicle, newState: VehicleSirenState)

}
