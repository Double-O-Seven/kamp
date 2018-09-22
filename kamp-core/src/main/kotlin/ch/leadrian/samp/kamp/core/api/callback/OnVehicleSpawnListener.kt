package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnVehicleSpawnListener {

    fun onVehicleSpawn(vehicle: Vehicle)

}
