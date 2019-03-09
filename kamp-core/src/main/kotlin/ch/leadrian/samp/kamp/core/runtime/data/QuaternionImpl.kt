package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutableQuaternion
import ch.leadrian.samp.kamp.core.api.data.Quaternion

internal data class QuaternionImpl(
        override val x: Float,
        override val y: Float,
        override val z: Float,
        override val w: Float
) : Quaternion {

    override fun toQuaternion(): Quaternion = this

    override fun toMutableQuaternion(): MutableQuaternion = MutableQuaternionImpl(x = x, y = y, z = z, w = w)

}