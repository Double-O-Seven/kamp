package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnVehicleDeathListener {

    fun onVehicleDeath(vehicle: Vehicle, killer: Player)

}
