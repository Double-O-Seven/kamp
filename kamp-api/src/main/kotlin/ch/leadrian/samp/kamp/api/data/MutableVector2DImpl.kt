package ch.leadrian.samp.kamp.api.data

internal data class MutableVector2DImpl(
        override var x: Float,
        override var y: Float
) : MutableVector2D {

    override fun toVector2D(): Vector2D = Vector2DImpl(
            x = x,
            y = y
    )

    override fun toMutableVector2D(): MutableVector2D = this

}