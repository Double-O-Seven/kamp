package ch.leadrian.samp.kamp.core.api.entity

interface VehicleComponents : HasVehicle {

    fun add(model: ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel)

    fun remove(model: ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel)

    operator fun get(slot: ch.leadrian.samp.kamp.core.api.constants.CarModType): ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel?

}