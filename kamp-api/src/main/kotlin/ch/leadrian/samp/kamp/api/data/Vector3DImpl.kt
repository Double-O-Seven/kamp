package ch.leadrian.samp.kamp.api.data

internal data class Vector3DImpl(
        override var x: Float,
        override var y: Float,
        override var z: Float
) : Vector3D {

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

}