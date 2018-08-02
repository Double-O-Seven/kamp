package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.PlayerKey

interface PlayerKeys : HasPlayer {

    val keys: Int

    val upDown: Int

    val leftRight: Int

    fun isKeyPressed(vararg keys: PlayerKey): Boolean

    fun isOnlyKeyPressed(vararg keys: PlayerKey): Boolean
}