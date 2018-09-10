package ch.leadrian.samp.kamp.core.api.data

interface Sphere : Shape3D {

    val x: Float

    val y: Float

    val z: Float

    val radius: Float

    fun toSphere(): Sphere

    fun toMutableSphere(): MutableSphere

    override fun contains(coordinates: Vector3D): Boolean {
        val dx = this.x - coordinates.x
        val dy = this.y - coordinates.y
        val dz = this.z - coordinates.z
        return (dx * dx + dy * dy + dz * dz) <= radius * radius
    }
}