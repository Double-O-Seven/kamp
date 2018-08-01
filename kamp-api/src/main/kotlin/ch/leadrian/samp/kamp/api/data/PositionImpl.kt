package ch.leadrian.samp.kamp.api.data

internal data class PositionImpl(
        override val x: Float,
        override val y: Float,
        override val z: Float,
        override val angle: Float
) : Position {

    override fun toPosition(): Position = this

    override fun toMutablePosition(): MutablePosition = MutablePositionImpl(
            x = x,
            y = y,
            z = z,
            angle = angle
    )

    override fun toVector3D(): Vector3D = this

    override fun toMutableVector3D(): MutableVector3D = MutableVector3DImpl(
            x = x,
            y = y,
            z = z
    )

    override fun toVector2D(): Vector2D = this

    override fun toMutableVector2D(): MutableVector2D = MutableVector2DImpl(
            x = x,
            y = y
    )

    override fun plus(other: Vector2D): Position = copy(
            x = this.x + other.x,
            y = this.y + other.y
    )

    override fun plus(other: Vector3D): Position = copy(
            x = this.x + other.x,
            y = this.y + other.y,
            z = this.z + other.z
    )

}