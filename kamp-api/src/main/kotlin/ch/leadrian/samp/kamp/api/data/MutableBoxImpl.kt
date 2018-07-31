package ch.leadrian.samp.kamp.api.data

internal data class MutableBoxImpl(
        override var minZ: Float,
        override var maxZ: Float,
        override var minX: Float,
        override var maxX: Float,
        override var minY: Float,
        override var maxY: Float
) : MutableBox {

    override fun toRectangle(): Rectangle = RectangleImpl(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY
    )

    override fun toMutableRectangle(): MutableRectangle = this

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