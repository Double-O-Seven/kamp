package ch.leadrian.samp.kamp.api.data

internal data class MutableRectangleImpl(
        override var minX: Float,
        override var maxX: Float,
        override var minY: Float,
        override var maxY: Float
) : MutableRectangle {

    override val area: Float
        get() = (maxX - minX) * (maxY - minY)

    override fun toRectangle(): Rectangle = RectangleImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY
    )

    override fun toMutableRectangle(): MutableRectangle = this
}