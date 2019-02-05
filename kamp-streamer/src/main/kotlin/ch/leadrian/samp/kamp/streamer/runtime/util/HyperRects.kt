package ch.leadrian.samp.kamp.streamer.runtime.util

import ch.leadrian.samp.kamp.core.api.data.Box
import ch.leadrian.samp.kamp.core.api.data.Circle
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import com.conversantmedia.util.collection.geometry.Rect2d
import com.conversantmedia.util.collection.geometry.Rect3d

fun Vector3D.toRect3d(offset: Float): Rect3d {
    val minX = x - offset
    val minY = y - offset
    val minZ = z - offset
    val maxX = x + offset
    val maxY = y + offset
    val maxZ = z + offset
    return Rect3d(
            minX.toDouble(),
            minY.toDouble(),
            minZ.toDouble(),
            maxX.toDouble(),
            maxY.toDouble(),
            maxZ.toDouble()
    )
}

fun Sphere.toRect3d(): Rect3d {
    val minX = x - radius
    val minY = y - radius
    val minZ = z - radius
    val maxX = x + radius
    val maxY = y + radius
    val maxZ = z + radius
    return Rect3d(
            minX.toDouble(),
            minY.toDouble(),
            minZ.toDouble(),
            maxX.toDouble(),
            maxY.toDouble(),
            maxZ.toDouble()
    )
}

fun Circle.toRect2d(): Rect2d {
    val minX = x - radius
    val minY = y - radius
    val maxX = x + radius
    val maxY = y + radius
    return Rect2d(
            minX.toDouble(),
            minY.toDouble(),
            maxX.toDouble(),
            maxY.toDouble()
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