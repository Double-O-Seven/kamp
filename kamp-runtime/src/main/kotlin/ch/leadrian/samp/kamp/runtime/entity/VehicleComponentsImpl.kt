package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.CarModType
import ch.leadrian.samp.kamp.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.VehicleComponents
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor

internal class VehicleComponentsImpl(
        override val vehicle: Vehicle,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : VehicleComponents {

    override fun add(model: VehicleComponentModel) {
        nativeFunctionExecutor.addVehicleComponent(vehicleid = vehicle.id.value, componentid = model.value)
    }

    override fun remove(model: VehicleComponentModel) {
        nativeFunctionExecutor.removeVehicleComponent(vehicleid = vehicle.id.value, componentid = model.value)
    }

    override fun get(slot: CarModType): VehicleComponentModel? =
            nativeFunctionExecutor
                    .getVehicleComponentInSlot(vehicleid = vehicle.id.value, slot = slot.value)
                    .takeIf { it != 0 }
                    ?.let { VehicleComponentModel[it] }
}