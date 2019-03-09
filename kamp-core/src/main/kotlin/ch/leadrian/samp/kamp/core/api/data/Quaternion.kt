package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.runtime.data.QuaternionImpl

interface Quaternion {

    val x: Float

    val y: Float

    val z: Float

    val w: Float

    fun toQuaternion(): Quaternion

    fun toMutableQuaternion(): MutableQuaternion
}

fun quaternionOf(x: Float, y: Float, z: Float, w: Float): Quaternion = QuaternionImpl(x = x, y = y, z = z, w = w)
