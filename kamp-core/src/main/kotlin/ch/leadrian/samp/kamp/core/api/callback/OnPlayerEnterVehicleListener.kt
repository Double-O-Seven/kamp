package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnPlayerEnterVehicleListener {

    fun onPlayerEnterVehicle(player: Player, vehicle: Vehicle, isPassenger: Boolean)

}
