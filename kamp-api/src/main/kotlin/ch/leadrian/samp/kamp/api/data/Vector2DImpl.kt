package ch.leadrian.samp.kamp.api.data

internal data class Vector2DImpl(
        override val x: Float,
        override val y: Float
) : Vector2D {

    override fun toVector2D(): Vector2D = this

    override fun toMutableVector2D(): MutableVector2D = MutableVector2DImpl(
            x = x,
            y = y
    )
}