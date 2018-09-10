package ch.leadrian.samp.kamp.core.api.data

internal data class MutableLocationImpl(
        override var x: Float,
        override var y: Float,
        override var z: Float,
        override var interiorId: Int,
        override var virtualWorldId: Int
) : MutableLocation {

    override fun toLocation(): Location = LocationImpl(
            x = x,
            y = y,
            z = z,
            interiorId = interiorId,
            virtualWorldId = virtualWorldId
    )

    override fun toMutableLocation(): MutableLocation = this

    override fun toVector3D(): Vector3D = Vector3DImpl(
            x = x,
            y = y,
            z = z
    )

    override fun toMutableVector3D(): MutableVector3D = this

    override fun toVector2D(): Vector2D = Vector2DImpl(
            x = x,
            y = y
    )

    override fun toMutableVector2D(): MutableVector2D = this

    override fun plus(other: Vector2D): MutableLocation = copy(
            x = this.x + other.x,
            y = this.y + other.y
    )

    override fun plus(other: Vector3D): MutableLocation = copy(
            x = this.x + other.x,
            y = this.y + other.y,
            z = this.z + other.z
    )

    override fun minus(other: Vector2D): MutableLocation = copy(
            x = this.x - other.x,
            y = this.y - other.y
    )

    override fun minus(other: Vector3D): MutableLocation = copy(
            x = this.x - other.x,
            y = this.y - other.y,
            z = this.z - other.z
    )

    override fun times(value: Float): MutableLocation = copy(
            x = this.x * value,
            y = this.y * value,
            z = this.z * value
    )

    override fun div(value: Float): MutableLocation = copy(
            x = this.x / value,
            y = this.y / value,
            z = this.z / value
    )
}