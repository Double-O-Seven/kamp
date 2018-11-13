package ch.leadrian.samp.kamp.geodata.util

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf

fun Short.uncompress(): Float = this.toFloat() / 8f

fun Int.uncompress(): Float = this.toFloat() / 8f

fun uncompressToVector2D(x: Short, y: Short): Vector2D =
        vector2DOf(x = x.uncompress(), y = y.uncompress())

fun uncompressToVector3D(x: Short, y: Short, z: Short): Vector3D =
        vector3DOf(x = x.uncompress(), y = y.uncompress(), z = z.uncompress())