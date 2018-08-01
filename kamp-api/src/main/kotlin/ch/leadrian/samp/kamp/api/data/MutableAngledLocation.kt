package ch.leadrian.samp.kamp.api.data

interface MutableAngledLocation : AngledLocation, MutableLocation, MutablePosition {

    override fun plus(other: Vector2D): MutableAngledLocation

    override fun plus(other: Vector3D): MutableAngledLocation
}