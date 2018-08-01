package ch.leadrian.samp.kamp.api.data

interface MutableVector2D : Vector2D {

    override var x: Float

    override var y: Float

    override fun plus(other: Vector2D): MutableVector2D

    operator fun plusAssign(other: Vector2D) {
        this.x += other.x
        this.y += other.y
    }
}