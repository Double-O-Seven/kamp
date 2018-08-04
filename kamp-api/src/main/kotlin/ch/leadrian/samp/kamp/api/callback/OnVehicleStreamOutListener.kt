package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnVehicleStreamOutListener {

    fun onVehicleStreamOut(vehicle: Vehicle, forPlayer: Player)

}
