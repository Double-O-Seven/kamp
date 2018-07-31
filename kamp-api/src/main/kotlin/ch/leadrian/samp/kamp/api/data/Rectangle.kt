package ch.leadrian.samp.kamp.api.data

interface Rectangle : Shape2D {

    val minX: Float

    val maxX: Float

    val minY: Float

    val maxY: Float

    fun toRectangle(): Rectangle

    fun toMutableRectangle(): MutableRectangle

    override val area: Float
        get() = (maxX - minX) * (maxY - minY)

    override fun contains(coordinates: Vector2D): Boolean =
            coordinates.x in (minX..maxX) && coordinates.y in (minY..maxY)
}
