package ch.leadrian.samp.kamp.api.data

interface AngledLocation : Location, Position {

    fun toAngledLocation(): AngledLocation

    fun toMutableAngledLocation(): MutableAngledLocation

    override fun plus(other: Vector2D): AngledLocation

    override fun plus(other: Vector3D): AngledLocation

    override fun minus(other: Vector2D): AngledLocation

    override fun minus(other: Vector3D): AngledLocation

    override fun times(value: Float): AngledLocation

    override fun div(value: Float): AngledLocation

}