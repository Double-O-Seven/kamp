package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.constants.VehicleColor
import ch.leadrian.samp.kamp.api.constants.VehicleModel
import ch.leadrian.samp.kamp.api.constants.VehicleModelInfoType
import ch.leadrian.samp.kamp.api.data.AngledLocation
import ch.leadrian.samp.kamp.api.data.Position
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.id.VehicleId

interface VehicleService {

    fun createVehicle(
            model: VehicleModel,
            position: Position,
            color1: VehicleColor,
            color: VehicleColor,
            respawnDelay: Int,
            addSiren: Boolean = false
    )

    fun createVehicle(
            model: VehicleModel,
            coordinates: Vector3D,
            rotation: Float,
            color1: VehicleColor,
            color: VehicleColor,
            respawnDelay: Int,
            addSiren: Boolean = false
    )

    fun createVehicle(
            model: VehicleModel,
            angledLocation: AngledLocation,
            color1: VehicleColor,
            color: VehicleColor,
            respawnDelay: Int,
            addSiren: Boolean = false
    )

    fun useManualVehicleLightsAndEngine()

    fun isValid(vehicleId: VehicleId): Boolean

    fun getVehicle(vehicleId: VehicleId): Vehicle

    fun getAllVehicles(): List<Vehicle>

    fun getPoolSize(): Int

    fun enableTirePopping(enable: Boolean)

    fun enableFriendlyFire()

    fun getModelInfo(vehicleModel: VehicleModel, type: VehicleModelInfoType): Vector3D
}