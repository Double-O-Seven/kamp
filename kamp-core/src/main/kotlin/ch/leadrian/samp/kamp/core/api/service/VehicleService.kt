package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId

interface VehicleService {

    fun createVehicle(
            model: ch.leadrian.samp.kamp.core.api.constants.VehicleModel,
            position: Position,
            color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor,
            color: ch.leadrian.samp.kamp.core.api.constants.VehicleColor,
            respawnDelay: Int,
            addSiren: Boolean = false
    ): Vehicle

    fun createVehicle(
            model: ch.leadrian.samp.kamp.core.api.constants.VehicleModel,
            coordinates: Vector3D,
            rotation: Float,
            color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor,
            color: ch.leadrian.samp.kamp.core.api.constants.VehicleColor,
            respawnDelay: Int,
            addSiren: Boolean = false
    ): Vehicle

    fun createVehicle(
            model: ch.leadrian.samp.kamp.core.api.constants.VehicleModel,
            angledLocation: ch.leadrian.samp.kamp.core.api.data.AngledLocation,
            color1: ch.leadrian.samp.kamp.core.api.constants.VehicleColor,
            color: ch.leadrian.samp.kamp.core.api.constants.VehicleColor,
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

    fun getModelInfo(vehicleModel: ch.leadrian.samp.kamp.core.api.constants.VehicleModel, type: ch.leadrian.samp.kamp.core.api.constants.VehicleModelInfoType): Vector3D
}