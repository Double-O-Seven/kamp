package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.VehicleTrailer
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry

internal class VehicleTrailerImpl(
        override val vehicle: Vehicle,
        private val vehicleRegistry: VehicleRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : VehicleTrailer {

    override fun attach(trailer: Vehicle) {
        nativeFunctionExecutor.attachTrailerToVehicle(trailerid = trailer.id.value, vehicleid = vehicle.id.value)
    }

    override fun detach() {
        nativeFunctionExecutor.detachTrailerFromVehicle(vehicle.id.value)
    }

    override val isAttached: Boolean
        get() = nativeFunctionExecutor.isTrailerAttachedToVehicle(vehicle.id.value)

    override val trailer: Vehicle?
        get() = nativeFunctionExecutor.getVehicleTrailer(vehicle.id.value).let { vehicleRegistry[it] }
}