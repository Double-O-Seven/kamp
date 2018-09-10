package ch.leadrian.samp.kamp.core.api.data

interface MutableAngledLocation : AngledLocation, MutableLocation, MutablePosition {

    override fun plus(other: Vector2D): MutableAngledLocation

    override fun plus(other: Vector3D): MutableAngledLocation

    override fun minus(other: Vector2D): MutableAngledLocation

    override fun minus(other: Vector3D): MutableAngledLocation

    override fun times(value: Float): MutableAngledLocation

    override fun div(value: Float): MutableAngledLocation
}