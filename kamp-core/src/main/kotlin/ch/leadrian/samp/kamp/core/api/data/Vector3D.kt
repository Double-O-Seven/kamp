package ch.leadrian.samp.kamp.core.api.data

interface Vector3D : Vector2D {

    companion object {

        val ORIGIN = vector3DOf(0f, 0f, 0f)
    }

    val z: Float

    fun distanceTo(other: Vector3D): Float {
        val dx = this.x - other.x
        val dy = this.y - other.y
        val dz = this.z - other.z
        return Math.sqrt((dx * dx + dy * dy + dz * dz).toDouble()).toFloat()
    }

    fun isInRange(other: Vector3D, distance: Float): Boolean {
        val dx = this.x - other.x
        val dy = this.y - other.y
        val dz = this.z - other.z
        return (dx * dx + dy * dy + dz * dz) <= distance * distance
    }

    fun toVector3D(): Vector3D

    fun toMutableVector3D(): MutableVector3D

    override fun plus(other: Vector2D): Vector3D

    operator fun plus(other: Vector3D): Vector3D

    override fun minus(other: Vector2D): Vector3D

    operator fun minus(other: Vector3D): Vector3D

    override fun times(value: Float): Vector3D

    override fun div(value: Float): Vector3D
}