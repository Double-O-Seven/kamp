package ch.leadrian.samp.kamp.api.data

interface MutablePosition : Position, MutableVector3D {

    override var angle: Float

    override fun plus(other: Vector2D): MutablePosition

    override fun plus(other: Vector3D): MutablePosition

    override fun minus(other: Vector2D): MutablePosition

    override fun minus(other: Vector3D): MutablePosition

    override fun times(value: Float): MutablePosition

}