package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.Circle
import ch.leadrian.samp.kamp.core.api.data.MutableCircle

internal data class MutableCircleImpl(
        override var x: Float,
        override var y: Float,
        override var radius: Float
) : MutableCircle {

    override val area: Float
        get() = radius * radius * Math.PI.toFloat()

    override fun toCircle(): Circle = CircleImpl(
            x = x,
            y = y,
            radius = radius
    )

    override fun toMutableCircle(): MutableCircle = this
}