package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.runtime.entity.InterceptableVehicle
import javax.inject.Singleton

@Singleton
internal class VehicleRegistry {

    private val vehicles: Array<InterceptableVehicle?> = arrayOfNulls(SAMPConstants.MAX_VEHICLES)

    fun register(vehicle: InterceptableVehicle) {
        if (vehicles[vehicle.id.value] != null) {
            throw IllegalStateException("There is already a vehicle with ID ${vehicle.id.value} registered")
        }
        vehicles[vehicle.id.value] = vehicle
    }

    fun unregister(vehicle: InterceptableVehicle) {
        if (vehicles[vehicle.id.value] !== vehicle) {
            throw IllegalStateException("Trying to unregister vehicle with ID ${vehicle.id.value} that is not registered")
        }
        vehicles[vehicle.id.value] = null
    }

    fun getVehicle(vehicleId: Int): InterceptableVehicle? =
            when (vehicleId) {
                in (0 until vehicles.size) -> vehicles[vehicleId]
                else -> null
            }

    fun getAllVehicles(): List<InterceptableVehicle> = vehicles.filterNotNull()

}