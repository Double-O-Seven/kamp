package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleModelInfoType
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.VehicleFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleService
@Inject
internal constructor(
        private val vehicleFactory: VehicleFactory,
        private val vehicleRegistry: VehicleRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    private val modelInfoCache = mutableMapOf<Pair<VehicleModel, VehicleModelInfoType>, Vector3D>()

    @JvmOverloads
    fun createVehicle(
            model: VehicleModel,
            colors: VehicleColors,
            position: Position,
            respawnDelay: Int?,
            addSiren: Boolean = false
    ): Vehicle = vehicleFactory.create(
            model = model,
            coordinates = position,
            rotation = position.angle,
            addSiren = addSiren,
            respawnDelay = respawnDelay,
            colors = colors
    )

    fun createVehicle(
            model: VehicleModel,
            colors: VehicleColors,
            coordinates: Vector3D,
            rotation: Float,
            respawnDelay: Int?,
            addSiren: Boolean = false
    ): Vehicle = vehicleFactory.create(
            model = model,
            coordinates = coordinates,
            rotation = rotation,
            addSiren = addSiren,
            respawnDelay = respawnDelay,
            colors = colors
    )

    fun createVehicle(
            model: VehicleModel,
            colors: VehicleColors,
            angledLocation: AngledLocation,
            respawnDelay: Int,
            addSiren: Boolean = false
    ): Vehicle = vehicleFactory.create(
            model = model,
            coordinates = angledLocation,
            rotation = angledLocation.angle,
            addSiren = addSiren,
            respawnDelay = respawnDelay,
            colors = colors
    ).apply {
        virtualWorldId = angledLocation.virtualWorldId
        interiorId = angledLocation.interiorId
    }

    fun useManualVehicleLightsAndEngine() {
        nativeFunctionExecutor.manualVehicleEngineAndLights()
    }

    fun isValidVehicle(vehicleId: VehicleId): Boolean = vehicleRegistry[vehicleId] != null

    fun getVehicle(vehicleId: VehicleId): Vehicle =
            vehicleRegistry[vehicleId] ?: throw NoSuchEntityException("No vehicle with ID ${vehicleId.value}")

    fun getAllVehicles(): List<Vehicle> = vehicleRegistry.getAll()

    fun getPoolSize(): Int = nativeFunctionExecutor.getVehiclePoolSize()

    fun enableTirePopping() {
        nativeFunctionExecutor.enableTirePopping(true)
    }

    fun disableTirePopping() {
        nativeFunctionExecutor.enableTirePopping(false)
    }

    fun enableFriendlyFire() {
        nativeFunctionExecutor.enableVehicleFriendlyFire()
    }

    fun getModelInfo(vehicleModel: VehicleModel, type: VehicleModelInfoType): Vector3D {
        return modelInfoCache.computeIfAbsent(vehicleModel to type) {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getVehicleModelInfo(model = vehicleModel.value, infotype = type.value, X = x, Y = y, Z = z)
            vector3DOf(x = x.value, y = y.value, z = z.value)
        }
    }
}