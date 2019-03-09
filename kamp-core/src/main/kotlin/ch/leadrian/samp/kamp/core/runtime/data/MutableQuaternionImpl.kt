package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutableQuaternion
import ch.leadrian.samp.kamp.core.api.data.Quaternion

internal data class MutableQuaternionImpl(
        override var x: Float,
        override var y: Float,
        override var z: Float,
        override var w: Float
) : MutableQuaternion {

    override fun toQuaternion(): Quaternion = QuaternionImpl(x = x, y = y, z = z, w = w)

    override fun toMutableQuaternion(): MutableQuaternion = this

}