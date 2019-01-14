package ch.leadrian.samp.kamp.streamer.runtime.util

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import com.conversantmedia.util.collection.geometry.Rect3d

fun Vector3D.toRect3d(offset: Float): Rect3d {
    val coordinates = this
    val minX = coordinates.x - offset
    val minY = coordinates.y - offset
    val minZ = coordinates.z - offset
    val maxX = coordinates.x + offset
    val maxY = coordinates.y + offset
    val maxZ = coordinates.z + offset

    return Rect3d(
            minX.toDouble(),
            minY.toDouble(),
            minZ.toDouble(),
            maxX.toDouble(),
            maxY.toDouble(),
            maxZ.toDouble()
    )
}