package ch.leadrian.samp.kamp.api.data

interface AngledLocation : Location, Position {

    fun toAngledLocation(): AngledLocation

    fun toMutableAngledLocation(): MutableAngledLocation

}