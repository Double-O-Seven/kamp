package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.CarModType
import ch.leadrian.samp.kamp.api.constants.VehicleComponentModel

interface VehicleComponents {

    fun add(model: VehicleComponentModel)

    fun remove(model: VehicleComponentModel)

    operator fun get(slot: CarModType): VehicleComponentModel?

}