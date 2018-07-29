package ch.leadrian.samp.kamp.api.data

interface Vector3D : Vector2D {

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
}