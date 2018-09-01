package ch.leadrian.samp.kamp.runtime.entity.factory

import ch.leadrian.samp.kamp.api.constants.VehicleModel
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.VehicleColors
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.InterceptableVehicle
import ch.leadrian.samp.kamp.runtime.entity.VehicleImpl
import ch.leadrian.samp.kamp.runtime.entity.interceptor.VehicleInterceptor
import ch.leadrian.samp.kamp.runtime.entity.interceptor.interceptorPriority
import ch.leadrian.samp.kamp.runtime.entity.registry.VehicleRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class VehicleFactory
@Inject
constructor(
        interceptors: Set<VehicleInterceptor>,
        private val vehicleRegistry: VehicleRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    private val interceptors: List<VehicleInterceptor> = interceptors
            .sortedByDescending { it.interceptorPriority }
            .toList()

    fun create(
            model: VehicleModel,
            colors: VehicleColors,
            coordinates: Vector3D,
            rotation: Float,
            addSiren: Boolean,
            respawnDelay: Int
    ): InterceptableVehicle {
        var vehicle: InterceptableVehicle = VehicleImpl(
                model = model,
                colors = colors,
                coordinates = coordinates,
                rotation = rotation,
                addSiren = addSiren,
                respawnDelay = respawnDelay,
                vehicleRegistry = vehicleRegistry,
                nativeFunctionExecutor = nativeFunctionExecutor
        )

        interceptors.forEach {
            vehicle = it.intercept(vehicle)
        }

        return vehicle
    }
}