package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnVehiclePaintjobListener {

    fun onVehiclePaintjob(player: Player, vehicle: Vehicle, paintjobId: Int): Boolean

}
