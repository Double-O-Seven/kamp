package ch.leadrian.samp.kamp.api.data

internal data class MutableAngledLocationImpl(
        override var x: Float,
        override var y: Float,
        override var z: Float,
        override var interiorId: Int,
        override var virtualWorldId: Int,
        override var angle: Float
) : MutableAngledLocation {

    override fun toAngledLocation(): AngledLocation = AngledLocationImpl(
            x = x,
            y = y,
            z = z,
            interiorId = interiorId,
            virtualWorldId = virtualWorldId,
            angle = angle
    )

    override fun toMutableAngledLocation(): MutableAngledLocation = this

    override fun toPosition(): Position = PositionImpl(
            x = x,
            y = y,
            z = z,
            angle = angle
    )

    override fun toMutablePosition(): MutablePosition = this

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
}