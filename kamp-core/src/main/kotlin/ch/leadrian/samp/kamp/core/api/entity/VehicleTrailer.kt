package ch.leadrian.samp.kamp.core.api.entity

interface VehicleTrailer : HasVehicle {

    fun attach(trailer: Vehicle)

    fun detach()

    val isAttached: Boolean

    val trailer: Vehicle?

}