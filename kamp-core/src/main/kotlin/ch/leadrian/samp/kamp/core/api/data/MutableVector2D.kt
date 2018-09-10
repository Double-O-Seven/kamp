package ch.leadrian.samp.kamp.core.api.data

interface MutableVector2D : Vector2D {

    override var x: Float

    override var y: Float

    override fun plus(other: Vector2D): MutableVector2D

    operator fun plusAssign(other: Vector2D) {
        this.x += other.x
        this.y += other.y
    }

    override fun minus(other: Vector2D): MutableVector2D

    operator fun minusAssign(other: Vector2D) {
        this.x -= other.x
        this.y -= other.y
    }

    override fun times(value: Float): MutableVector2D

    operator fun timesAssign(value: Float) {
        this.x *= value
        this.y *= value
    }

    override fun div(value: Float): MutableVector2D

    operator fun divAssign(value: Float) {
        this.x /= value
        this.y /= value
    }
}