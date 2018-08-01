package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.constants.VehicleModel
import ch.leadrian.samp.kamp.api.constants.VehicleModelInfoType
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.id.VehicleId

interface VehicleService {

    fun useManualVehicleLightsAndEngine()

    fun getVehicle(vehicleId: VehicleId): Vehicle

    fun getAllVehicles(): List<Vehicle>

    fun getPoolSize(): Int

    fun enableTirePopping()

    fun enableFriendlyFire()

    fun getModelInfo(vehicleModel: VehicleModel, type: VehicleModelInfoType): Vector3D
}