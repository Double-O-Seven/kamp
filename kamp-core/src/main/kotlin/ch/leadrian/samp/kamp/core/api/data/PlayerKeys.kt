package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.PlayerKey

data class PlayerKeys
internal constructor(
        val keys: Int,
        val upDown: Int,
        val leftRight: Int
) {

    fun isKeyPressed(vararg keys: PlayerKey): Boolean =
            keys.all { this.keys and it.value != 0 }

    fun isOnlyKeyPressed(vararg keys: PlayerKey): Boolean =
            keys.fold(0) { expectedKeys, key -> expectedKeys or key.value } == this.keys
}