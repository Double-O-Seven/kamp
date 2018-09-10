package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleModelInfoType
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId

interface VehicleService {

    fun createVehicle(
            model: VehicleModel,
            position: Position,
            color1: VehicleColor,
            color: VehicleColor,
            respawnDelay: Int,
            addSiren: Boolean = false
    ): Vehicle

    fun createVehicle(
            model: VehicleModel,
            coordinates: Vector3D,
            rotation: Float,
            color1: VehicleColor,
            color: VehicleColor,
            respawnDelay: Int,
            addSiren: Boolean = false
    ): Vehicle

    fun createVehicle(
            model: VehicleModel,
            angledLocation: AngledLocation,
            color1: VehicleColor,
            color: VehicleColor,
            respawnDelay: Int,
            addSiren: Boolean = false
    ): Vehicle

    fun useManualVehicleLightsAndEngine()

    fun isValid(vehicleId: VehicleId): Boolean

    fun getVehicle(vehicleId: VehicleId): Vehicle

    fun getAllVehicles(): List<Vehicle>

    fun getPoolSize(): Int

    fun enableTirePopping(enable: Boolean)

    fun enableFriendlyFire()

    fun getModelInfo(vehicleModel: VehicleModel, type: VehicleModelInfoType): Vector3D
}