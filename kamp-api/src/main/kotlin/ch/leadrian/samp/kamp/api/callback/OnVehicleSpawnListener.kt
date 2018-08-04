package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnVehicleSpawnListener {

    fun onVehicleSpawn(vehicle: Vehicle): Boolean

}
