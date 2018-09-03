package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.constants.VehicleModel
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.VehicleColors
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.VehicleImpl
import ch.leadrian.samp.kamp.runtime.entity.registry.VehicleRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
            addSiren: Boolean,
            respawnDelay: Int
    ): VehicleImpl {
        val vehicle = VehicleImpl(
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
        return vehicle
    }
}