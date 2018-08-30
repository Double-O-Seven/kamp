package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnVehicleModListener {

    fun onVehicleMod(player: Player, vehicle: Vehicle, componentModel: VehicleComponentModel): Result

    sealed class Result(val value: Boolean) {

        object Sync : Result(true)

        object Desync : Result(false)
    }

}
