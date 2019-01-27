package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.Circle
import ch.leadrian.samp.kamp.core.api.data.MutableCircle

internal data class CircleImpl(
        override val x: Float,
        override val y: Float,
        override val radius: Float
) : Circle {

    override val area: Float = radius * radius * Math.PI.toFloat()

    override fun toCircle(): Circle = this

    override fun toMutableCircle(): MutableCircle = MutableCircleImpl(
            x = x,
            y = y,
            radius = radius
    )
}