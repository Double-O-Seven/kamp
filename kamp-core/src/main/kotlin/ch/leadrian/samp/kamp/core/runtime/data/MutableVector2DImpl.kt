package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutableVector2D
import ch.leadrian.samp.kamp.core.api.data.Vector2D

internal data class MutableVector2DImpl(
        override var x: Float,
        override var y: Float
) : MutableVector2D {

    override fun toVector2D(): Vector2D = Vector2DImpl(
            x = x,
            y = y
    )

    override fun toMutableVector2D(): MutableVector2D = this

    override fun plus(other: Vector2D): MutableVector2D = copy(
            x = this.x + other.x,
            y = this.y + other.y
    )

    override fun minus(other: Vector2D): MutableVector2D = copy(
            x = this.x - other.x,
            y = this.y - other.y
    )

    override fun times(value: Float): MutableVector2D = copy(
            x = this.x * value,
            y = this.y * value
    )

    override fun div(value: Float): MutableVector2D = copy(
            x = this.x / value,
            y = this.y / value
    )

}