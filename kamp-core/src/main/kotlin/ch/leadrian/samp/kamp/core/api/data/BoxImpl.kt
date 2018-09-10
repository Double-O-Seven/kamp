package ch.leadrian.samp.kamp.core.api.data

internal data class BoxImpl(
        override val minZ: Float,
        override val maxZ: Float,
        override val minX: Float,
        override val maxX: Float,
        override val minY: Float,
        override val maxY: Float
) : ch.leadrian.samp.kamp.core.api.data.Box {

    override val volume: Float = (maxX - minX) * (maxY - minY) * (maxZ - minZ)

    override fun toBox(): ch.leadrian.samp.kamp.core.api.data.Box = this

    override fun toMutableBox(): MutableBox = MutableBoxImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY,
            minZ = minZ,
            maxZ = maxZ
    )
}