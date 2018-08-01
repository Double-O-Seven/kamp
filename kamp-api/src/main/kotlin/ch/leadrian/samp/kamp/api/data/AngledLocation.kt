package ch.leadrian.samp.kamp.api.data

interface AngledLocation : Location, Position {

    fun toAngledLocation(): AngledLocation

    fun toMutableAngledLocation(): MutableAngledLocation

    override fun plus(other: Vector2D): AngledLocation

    override fun plus(other: Vector3D): AngledLocation

}