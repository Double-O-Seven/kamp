package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.PlayerKey
import ch.leadrian.samp.kamp.core.runtime.data.PlayerKeysImpl

interface PlayerKeys {

    val keys: Int

    val upDown: Int

    val leftRight: Int

    fun isPressed(vararg keys: PlayerKey): Boolean =
            keys.all { this.keys and it.value != 0 }

    fun isPressedExactly(vararg keys: PlayerKey): Boolean =
            keys.fold(0) { expectedKeys, key -> expectedKeys or key.value } == this.keys

    fun toPlayerKeys(): PlayerKeys

    fun toMutablePlayerKeys(): MutablePlayerKeys
}

fun playerKeysOf(keys: Int = 0, upDown: Int = 0, leftRight: Int = 0): PlayerKeys =
        PlayerKeysImpl(keys = keys, leftRight = leftRight, upDown = upDown)

fun playerKeysOf(vararg keys: PlayerKey, upDown: Int = 0, leftRight: Int = 0): PlayerKeys {
    var pressedKeys = 0
    keys.forEach { key ->
        pressedKeys = pressedKeys or key.value
    }
    return playerKeysOf(keys = pressedKeys, upDown = upDown, leftRight = leftRight)
}