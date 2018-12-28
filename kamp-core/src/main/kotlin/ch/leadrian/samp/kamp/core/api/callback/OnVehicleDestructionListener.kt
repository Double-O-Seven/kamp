package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Vehicle

@Deprecated("This is unnecessary")
interface OnVehicleDestructionListener {

    fun onVehicleDestruction(vehicle: Vehicle)

}
