package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.PlayerKey
import ch.leadrian.samp.kamp.core.runtime.data.MutablePlayerKeysImpl

interface MutablePlayerKeys : PlayerKeys {

    override var keys: Int

    override var upDown: Int

    override var leftRight: Int

    fun press(vararg keys: PlayerKey) {
        keys.forEach { key ->
            this.keys = this.keys or key.value
        }
    }

    fun release(vararg keys: PlayerKey) {
        keys.forEach { key ->
            this.keys = this.keys and (key.value.inv())
        }
    }

    fun pressExactly(vararg keys: PlayerKey) {
        this.keys = keys.fold(0) { pressedKeys, key -> pressedKeys or key.value }
    }

}

fun mutablePlayerKeysOf(keys: Int = 0, upDown: Int = 0, leftRight: Int = 0): MutablePlayerKeys =
        MutablePlayerKeysImpl(keys = keys, leftRight = leftRight, upDown = upDown)

fun mutablePlayerKeysOf(vararg keys: PlayerKey, upDown: Int = 0, leftRight: Int = 0): MutablePlayerKeys {
    var pressedKeys = 0
    keys.forEach { key ->
        pressedKeys = pressedKeys or key.value
    }
    return mutablePlayerKeysOf(keys = pressedKeys, upDown = upDown, leftRight = leftRight)
}