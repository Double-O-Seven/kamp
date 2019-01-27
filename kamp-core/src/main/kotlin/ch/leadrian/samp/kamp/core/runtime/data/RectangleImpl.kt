package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutableRectangle
import ch.leadrian.samp.kamp.core.api.data.Rectangle

internal data class RectangleImpl(
        override val minX: Float,
        override val maxX: Float,
        override val minY: Float,
        override val maxY: Float
) : Rectangle {

    override val width: Float = maxX - minX

    override val height: Float = maxY - minY

    override val area: Float = width * height

    override fun toRectangle(): Rectangle = this

    override fun toMutableRectangle(): MutableRectangle = MutableRectangleImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY
    )
}