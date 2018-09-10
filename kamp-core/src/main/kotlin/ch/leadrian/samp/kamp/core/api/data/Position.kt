package ch.leadrian.samp.kamp.core.api.data

interface Position : Vector3D {

    val angle: Float

    fun toPosition(): Position

    fun toMutablePosition(): MutablePosition

    override fun plus(other: Vector2D): Position

    override fun plus(other: Vector3D): Position

    override fun minus(other: Vector2D): Position

    override fun minus(other: Vector3D): Position

    override fun times(value: Float): Position

    override fun div(value: Float): Position

}