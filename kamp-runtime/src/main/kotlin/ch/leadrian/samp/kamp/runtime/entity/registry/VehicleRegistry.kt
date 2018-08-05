package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Vehicle
import javax.inject.Singleton

@Singleton
class VehicleRegistry {

    private val vehicles: Array<Vehicle?> = arrayOfNulls(SAMPConstants.MAX_VEHICLES)

    fun register(vehicle: Vehicle) {
        if (vehicles[vehicle.id.value] != null) {
            throw IllegalStateException("There is already a vehicle with ID ${vehicle.id.value} registered")
        }
        vehicles[vehicle.id.value] = vehicle
    }

    fun unregister(vehicle: Vehicle) {
        if (vehicles[vehicle.id.value] !== vehicle) {
            throw IllegalStateException("Trying to unregister vehicle with ID ${vehicle.id.value} that is not registered")
        }
        vehicles[vehicle.id.value] = null
    }

    fun getVehicle(vehicleId: Int): Vehicle? =
            when {
                0 <= vehicleId && vehicleId < vehicles.size -> vehicles[vehicleId]
                else -> null
            }

    fun getAllVehicles(): List<Vehicle> = vehicles.filterNotNull()

}