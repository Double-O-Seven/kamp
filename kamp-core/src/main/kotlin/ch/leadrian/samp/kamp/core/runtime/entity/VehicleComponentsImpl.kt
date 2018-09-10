package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.VehicleComponents
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

internal class VehicleComponentsImpl(
        override val vehicle: Vehicle,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : VehicleComponents {

    override fun add(model: ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel) {
        nativeFunctionExecutor.addVehicleComponent(vehicleid = vehicle.id.value, componentid = model.value)
    }

    override fun remove(model: ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel) {
        nativeFunctionExecutor.removeVehicleComponent(vehicleid = vehicle.id.value, componentid = model.value)
    }

    override fun get(slot: ch.leadrian.samp.kamp.core.api.constants.CarModType): ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel? =
            nativeFunctionExecutor
                    .getVehicleComponentInSlot(vehicleid = vehicle.id.value, slot = slot.value)
                    .takeIf { it != 0 }
                    ?.let { ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel[it] }
}