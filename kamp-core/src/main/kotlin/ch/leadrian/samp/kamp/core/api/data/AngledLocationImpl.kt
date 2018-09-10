package ch.leadrian.samp.kamp.core.api.data

internal data class AngledLocationImpl(
        override val x: Float,
        override val y: Float,
        override val z: Float,
        override val interiorId: Int,
        override val virtualWorldId: Int,
        override val angle: Float
) : ch.leadrian.samp.kamp.core.api.data.AngledLocation {

    override fun toAngledLocation(): ch.leadrian.samp.kamp.core.api.data.AngledLocation = this

    override fun toMutableAngledLocation(): MutableAngledLocation = MutableAngledLocationImpl(
            x = x,
            y = y,
            z = z,
            interiorId = interiorId,
            virtualWorldId = virtualWorldId,
            angle = angle
    )

    override fun toPosition(): Position = this

    override fun toMutablePosition(): MutablePosition = MutablePositionImpl(
            x = x,
            y = y,
            z = z,
            angle = angle
    )

    override fun toLocation(): Location = this

    override fun toMutableLocation(): MutableLocation = MutableLocationImpl(
            x = x,
            y = y,
            z = z,
            interiorId = interiorId,
            virtualWorldId = virtualWorldId
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

    override fun plus(other: Vector2D): ch.leadrian.samp.kamp.core.api.data.AngledLocation = copy(
            x = this.x + other.x,
            y = this.y + other.y
    )

    override fun plus(other: Vector3D): ch.leadrian.samp.kamp.core.api.data.AngledLocation = copy(
            x = this.x + other.x,
            y = this.y + other.y,
            z = this.z + other.z
    )

    override fun minus(other: Vector2D): ch.leadrian.samp.kamp.core.api.data.AngledLocation = copy(
            x = this.x - other.x,
            y = this.y - other.y
    )

    override fun minus(other: Vector3D): ch.leadrian.samp.kamp.core.api.data.AngledLocation = copy(
            x = this.x - other.x,
            y = this.y - other.y,
            z = this.z - other.z
    )

    override fun times(value: Float): ch.leadrian.samp.kamp.core.api.data.AngledLocation = copy(
            x = this.x * value,
            y = this.y * value,
            z = this.z * value
    )

    override fun div(value: Float): ch.leadrian.samp.kamp.core.api.data.AngledLocation = copy(
            x = this.x / value,
            y = this.y / value,
            z = this.z / value
    )
}