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

    override fun isKeyPressed(vararg keys: PlayerKey): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isOnlyKeyPressed(vararg keys: PlayerKey): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}