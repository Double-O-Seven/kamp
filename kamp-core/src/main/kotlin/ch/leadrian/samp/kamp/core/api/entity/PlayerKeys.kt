package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.PlayerKey

class PlayerKeys
internal constructor(
        val keys: Int,
        val upDown: Int,
        val leftRight: Int,
        override val player: Player
) : HasPlayer {

    fun isKeyPressed(vararg keys: PlayerKey): Boolean =
            keys.all { this.keys and it.value != 0 }

    fun isOnlyKeyPressed(vararg keys: PlayerKey): Boolean =
            keys.fold(0) { expectedKeys, key -> expectedKeys or key.value } == this.keys
}