package ch.leadrian.samp.kamp.core.api.data

interface Vector2D {

    val x: Float

    val y: Float

    fun distanceTo(other: Vector2D): Float {
        val dx = this.x - other.x
        val dy = this.y - other.y
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    fun isInRange(other: Vector2D, distance: Float): Boolean {
        val dx = this.x - other.x
        val dy = this.y - other.y
        return (dx * dx + dy * dy) <= distance * distance
    }

    fun toVector2D(): Vector2D

    fun toMutableVector2D(): MutableVector2D

    operator fun plus(other: Vector2D): Vector2D

    operator fun minus(other: Vector2D): Vector2D

    operator fun times(value: Float): Vector2D

    operator fun div(value: Float): Vector2D
}

fun <T : Vector2D> T.abs(): Double = Math.sqrt((x * x + y * y).toDouble())

fun <T : Vector3D> T.abs(): Double = Math.sqrt((x * x + y * y + z * z).toDouble())
