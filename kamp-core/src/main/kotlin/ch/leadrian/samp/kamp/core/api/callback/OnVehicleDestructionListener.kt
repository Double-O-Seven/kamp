package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnVehicleDestructionListener {

    fun onVehicleDestruction(vehicle: Vehicle)

}
