package ch.leadrian.samp.kamp.core.api.data

internal data class MutableRectangleImpl(
        override var minX: Float,
        override var maxX: Float,
        override var minY: Float,
        override var maxY: Float
) : MutableRectangle {

    override val width: Float
        get() = maxX - minX

    override val height: Float
        get() = maxY - minY

    override val area: Float
        get() = width * height

    override fun toRectangle(): Rectangle = RectangleImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY
    )

    override fun toMutableRectangle(): MutableRectangle = this
}