package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.PlayerKey
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.PlayerKeys

internal class PlayerKeysImpl(
        override val keys: Int,
        override val upDown: Int,
        override val leftRight: Int,
        override val player: Player
) : PlayerKeys {

    override fun isKeyPressed(vararg keys: PlayerKey): Boolean =
            keys.all { this.keys and it.value != 0 }

    override fun isOnlyKeyPressed(vararg keys: PlayerKey): Boolean =
            keys.fold(0) { expectedKeys, key -> expectedKeys or key.value } == this.keys
}