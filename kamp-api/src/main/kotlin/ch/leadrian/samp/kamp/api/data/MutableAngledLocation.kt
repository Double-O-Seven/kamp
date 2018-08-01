package ch.leadrian.samp.kamp.api.data

interface MutableAngledLocation : AngledLocation, MutableLocation, MutablePosition {

    override fun plus(other: Vector2D): MutableAngledLocation

    override fun plus(other: Vector3D): MutableAngledLocation

    override fun minus(other: Vector2D): MutableAngledLocation

    override fun minus(other: Vector3D): MutableAngledLocation

    override fun times(value: Float): MutableAngledLocation
}