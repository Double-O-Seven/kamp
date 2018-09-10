package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry

class VehicleTrailer
internal constructor(
        override val vehicle: Vehicle,
        private val vehicleRegistry: VehicleRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasVehicle {

    fun attach(trailer: Vehicle) {
        nativeFunctionExecutor.attachTrailerToVehicle(trailerid = trailer.id.value, vehicleid = vehicle.id.value)
    }

    fun detach() {
        nativeFunctionExecutor.detachTrailerFromVehicle(vehicle.id.value)
    }

    val isAttached: Boolean
        get() = nativeFunctionExecutor.isTrailerAttachedToVehicle(vehicle.id.value)

    val trailer: Vehicle?
        get() = nativeFunctionExecutor.getVehicleTrailer(vehicle.id.value).let { vehicleRegistry[it] }
}