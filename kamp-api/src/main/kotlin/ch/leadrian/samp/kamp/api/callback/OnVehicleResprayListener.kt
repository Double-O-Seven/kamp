package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.data.VehicleColors
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnVehicleResprayListener {

    fun onVehicleRespray(player: Player, vehicle: Vehicle, colors: VehicleColors)

}
