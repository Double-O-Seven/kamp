package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.CarModType
import ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class VehicleComponents
internal constructor(
        override val vehicle: Vehicle,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasVehicle {

    fun add(model: VehicleComponentModel) {
        nativeFunctionExecutor.addVehicleComponent(vehicleid = vehicle.id.value, componentid = model.value)
    }

    fun remove(model: VehicleComponentModel) {
        nativeFunctionExecutor.removeVehicleComponent(vehicleid = vehicle.id.value, componentid = model.value)
    }

    operator fun get(slot: CarModType): VehicleComponentModel? =
            nativeFunctionExecutor
                    .getVehicleComponentInSlot(vehicleid = vehicle.id.value, slot = slot.value)
                    .takeIf { it != 0 }
                    ?.let { ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel[it] }
}