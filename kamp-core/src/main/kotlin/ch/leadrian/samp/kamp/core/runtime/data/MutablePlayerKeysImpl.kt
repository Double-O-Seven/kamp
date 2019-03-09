package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutablePlayerKeys
import ch.leadrian.samp.kamp.core.api.data.PlayerKeys

internal data class MutablePlayerKeysImpl(
        override var keys: Int,
        override var leftRight: Int,
        override var upDown: Int
) : MutablePlayerKeys {

    override fun toPlayerKeys(): PlayerKeys = PlayerKeysImpl(keys = keys, leftRight = leftRight, upDown = upDown)

    override fun toMutablePlayerKeys(): MutablePlayerKeys = this

}