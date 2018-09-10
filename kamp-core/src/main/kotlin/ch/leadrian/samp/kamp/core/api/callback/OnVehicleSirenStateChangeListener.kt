package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnVehicleSirenStateChangeListener {

    fun onVehicleSirenStateChange(player: Player, vehicle: Vehicle, newState: VehicleSirenState)

}
