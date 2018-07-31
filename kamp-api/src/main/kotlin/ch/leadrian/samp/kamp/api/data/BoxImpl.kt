package ch.leadrian.samp.kamp.api.data

internal data class BoxImpl(
        override val minZ: Float,
        override val maxZ: Float,
        override val minX: Float,
        override val maxX: Float,
        override val minY: Float,
        override val maxY: Float
) : Box {

    override fun toRectangle(): Rectangle = this

    override fun toMutableRectangle(): MutableRectangle = MutableRectangleImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY
    )

    override fun toBox(): Box = this

    override fun toMutableBox(): MutableBox = MutableBoxImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY,
            minZ = minZ,
            maxZ = maxZ
    )
}