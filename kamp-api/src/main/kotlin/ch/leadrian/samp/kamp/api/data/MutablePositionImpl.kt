package ch.leadrian.samp.kamp.api.data

internal data class MutablePositionImpl(
        override var x: Float,
        override var y: Float,
        override var z: Float,
        override var angle: Float
) : MutablePosition {

    override fun toPosition(): Position = PositionImpl(
            x = x,
            y = y,
            z = z,
            angle = angle
    )

    override fun toMutablePosition(): MutablePosition = this

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

    override fun plus(other: Vector2D): MutablePosition = copy(
            x = this.x + other.x,
            y = this.y + other.y
    )

    override fun plus(other: Vector3D): MutablePosition = copy(
            x = this.x + other.x,
            y = this.y + other.y,
            z = this.z + other.z
    )

    override fun minus(other: Vector2D): MutablePosition = copy(
            x = this.x - other.x,
            y = this.y - other.y
    )

    override fun minus(other: Vector3D): MutablePosition = copy(
            x = this.x - other.x,
            y = this.y - other.y,
            z = this.z - other.z
    )

    override fun times(value: Float): MutablePosition = copy(
            x = this.x * value,
            y = this.y * value,
            z = this.z * value
    )

}