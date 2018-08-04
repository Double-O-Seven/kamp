package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.PlayerKeys

interface OnPlayerKeyStateChangeListener {

    fun onPlayerKeyStateChange(oldKeys: PlayerKeys, newKeys: PlayerKeys)

}
