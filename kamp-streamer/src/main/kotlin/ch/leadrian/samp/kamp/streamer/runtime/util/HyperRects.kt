package ch.leadrian.samp.kamp.streamer.runtime.util

import ch.leadrian.samp.kamp.core.api.data.Box
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import com.conversantmedia.util.collection.geometry.Rect2d
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

fun Rectangle.toRect2d(): Rect2d {
    return Rect2d(minX.toDouble(), minY.toDouble(), maxX.toDouble(), maxY.toDouble())
}

fun Box.toRect3d(): Rect3d {
    return Rect3d(
            minX.toDouble(),
            minY.toDouble(),
            minZ.toDouble(),
            maxX.toDouble(),
            maxY.toDouble(),
            maxZ.toDouble()
    )
}