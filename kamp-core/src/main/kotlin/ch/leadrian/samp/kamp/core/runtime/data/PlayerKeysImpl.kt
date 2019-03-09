package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutablePlayerKeys
import ch.leadrian.samp.kamp.core.api.data.PlayerKeys

internal class PlayerKeysImpl(
        override val keys: Int,
        override val leftRight: Int,
        override val upDown: Int
) : PlayerKeys {

    override fun toPlayerKeys(): PlayerKeys = this

    override fun toMutablePlayerKeys(): MutablePlayerKeys =
            MutablePlayerKeysImpl(keys = keys, leftRight = leftRight, upDown = upDown)

}