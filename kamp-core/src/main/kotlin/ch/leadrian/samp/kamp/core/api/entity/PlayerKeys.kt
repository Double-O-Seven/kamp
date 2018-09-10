package ch.leadrian.samp.kamp.core.api.entity

interface PlayerKeys : HasPlayer {

    val keys: Int

    val upDown: Int

    val leftRight: Int

    fun isKeyPressed(vararg keys: ch.leadrian.samp.kamp.core.api.constants.PlayerKey): Boolean

    fun isOnlyKeyPressed(vararg keys: ch.leadrian.samp.kamp.core.api.constants.PlayerKey): Boolean
}