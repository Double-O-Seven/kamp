package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.VehicleModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import javax.inject.Inject

internal class VehicleFactory
@Inject
constructor(
        private val vehicleRegistry: VehicleRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun create(
            model: VehicleModel,
            colors: VehicleColors,
            coordinates: Vector3D,
            rotation: Float,
            respawnDelay: Int?,
            addSiren: Boolean
    ): Vehicle {
        val vehicle = Vehicle(
                model = model,
                colors = colors,
                coordinates = coordinates,
                rotation = rotation,
                addSiren = addSiren,
                respawnDelay = respawnDelay,
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
        vehicleRegistry.register(vehicle)
        vehicle.onDestroy { vehicleRegistry.unregister(this) }
        return vehicle
    }
}