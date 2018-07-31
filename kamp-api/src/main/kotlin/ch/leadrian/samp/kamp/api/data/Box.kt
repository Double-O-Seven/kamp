package ch.leadrian.samp.kamp.api.data

interface Box : Shape3D {

    val minX: Float

    val maxX: Float

    val minY: Float

    val maxY: Float

    val minZ: Float

    val maxZ: Float

    override fun contains(coordinates: Vector3D): Boolean =
            coordinates.x in (minX..maxX) && coordinates.y in (minY..maxY) && coordinates.z in (minZ..maxZ)

    fun toBox(): Box

    fun toMutableBox(): MutableBox
}