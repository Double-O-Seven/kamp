package ch.leadrian.samp.kamp.core.api.data

internal data class RectangleImpl(
        override val minX: Float,
        override val maxX: Float,
        override val minY: Float,
        override val maxY: Float
) : Rectangle {

    override val area: Float = (maxX - minX) * (maxY - minY)

    override fun toRectangle(): Rectangle = this

    override fun toMutableRectangle(): MutableRectangle = MutableRectangleImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY
    )
}