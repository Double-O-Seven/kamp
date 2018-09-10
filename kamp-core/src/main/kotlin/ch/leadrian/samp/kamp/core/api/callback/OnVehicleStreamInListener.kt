package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnVehicleStreamInListener {

    fun onVehicleStreamIn(vehicle: Vehicle, forPlayer: Player)

}
