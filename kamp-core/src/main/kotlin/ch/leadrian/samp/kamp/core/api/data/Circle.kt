package ch.leadrian.samp.kamp.core.api.data

interface Circle : Shape2D {

    val x: Float

    val y: Float

    val radius: Float

    fun toCircle(): Circle

    fun toMutableCircle(): MutableCircle

    override fun contains(coordinates: Vector2D): Boolean {
        val dx = this.x - coordinates.x
        val dy = this.y - coordinates.y
        return (dx * dx + dy * dy) <= radius * radius
    }

}