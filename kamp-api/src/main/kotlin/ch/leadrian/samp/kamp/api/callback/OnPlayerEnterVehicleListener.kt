package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnPlayerEnterVehicleListener {

    fun onPlayerEnterVehicle(player: Player, vehicle: Vehicle, isPassenger: Boolean)

}
