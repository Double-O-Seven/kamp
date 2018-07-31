package ch.leadrian.samp.kamp.api.data

interface Box : Rectangle, Shape3D {

    val minZ: Float

    val maxZ: Float

    override val volume: Float
        get() = area * (maxZ - minZ)

    override fun contains(coordinates: Vector3D): Boolean =
            coordinates.z in (minZ..maxZ) && contains(coordinates as Vector2D)

    fun toBox(): Box

    fun toMutableBox(): MutableBox
}