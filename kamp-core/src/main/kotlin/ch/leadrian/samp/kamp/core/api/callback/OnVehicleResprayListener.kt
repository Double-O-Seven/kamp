package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnVehicleResprayListener {

    fun onVehicleRespray(player: Player, vehicle: Vehicle, colors: VehicleColors)

}
