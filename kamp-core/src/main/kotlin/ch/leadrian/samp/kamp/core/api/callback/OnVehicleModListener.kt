package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnVehicleModListener {

    fun onVehicleMod(player: Player, vehicle: Vehicle, componentModel: ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel): Result

    sealed class Result(val value: Boolean) {

        object Sync : Result(true)

        object Desync : Result(false)
    }

}
