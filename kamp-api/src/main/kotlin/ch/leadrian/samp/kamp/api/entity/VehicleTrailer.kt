package ch.leadrian.samp.kamp.api.entity

interface VehicleTrailer {

    fun attach(trailer: Vehicle)

    fun detach()

    val isAttached: Boolean

    val trailer: Vehicle?

}