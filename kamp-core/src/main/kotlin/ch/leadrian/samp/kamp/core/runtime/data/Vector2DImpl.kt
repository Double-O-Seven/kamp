package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutableVector2D
import ch.leadrian.samp.kamp.core.api.data.Vector2D

internal data class Vector2DImpl(
        override val x: Float,
        override val y: Float
) : Vector2D {

    override fun toVector2D(): Vector2D = this

    override fun toMutableVector2D(): MutableVector2D = MutableVector2DImpl(
            x = x,
            y = y
    )

    override fun plus(other: Vector2D): Vector2D = copy(
            x = this.x + other.x,
            y = this.y + other.y
    )

    override fun minus(other: Vector2D): Vector2D = copy(
            x = this.x - other.x,
            y = this.y - other.y
    )

    override fun times(value: Float): Vector2D = copy(
            x = this.x * value,
            y = this.y * value
    )

    override fun div(value: Float): Vector2D = copy(
            x = this.x / value,
            y = this.y / value
    )
}