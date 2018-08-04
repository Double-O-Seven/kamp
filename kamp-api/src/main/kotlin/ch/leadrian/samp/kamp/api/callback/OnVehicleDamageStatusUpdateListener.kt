package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnVehicleDamageStatusUpdateListener {

    fun onVehicleDamageStatusUpdate(vehicle: Vehicle, player: Player): Boolean

}
