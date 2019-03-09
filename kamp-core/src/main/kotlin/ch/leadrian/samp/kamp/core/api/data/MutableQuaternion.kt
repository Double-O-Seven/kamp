package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.runtime.data.MutableQuaternionImpl

interface MutableQuaternion : Quaternion {

    override var x: Float

    override var y: Float

    override var z: Float

    override var w: Float
}

fun mutableQuaternionOf(x: Float, y: Float, z: Float, w: Float): MutableQuaternion =
        MutableQuaternionImpl(x = x, y = y, z = z, w = w)