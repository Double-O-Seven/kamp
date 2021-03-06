package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.Box
import ch.leadrian.samp.kamp.core.api.data.MutableBox

internal data class MutableBoxImpl(
        override var minZ: Float,
        override var maxZ: Float,
        override var minX: Float,
        override var maxX: Float,
        override var minY: Float,
        override var maxY: Float
) : MutableBox {

    override val volume: Float
        get() = (maxX - minX) * (maxY - minY) * (maxZ - minZ)

    override fun toBox(): Box = BoxImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY,
            minZ = minZ,
            maxZ = maxZ
    )

    override fun toMutableBox(): MutableBox = this
}