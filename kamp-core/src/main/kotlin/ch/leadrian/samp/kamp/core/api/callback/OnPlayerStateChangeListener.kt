package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerStateChangeListener {

    fun onPlayerStateChange(player: Player, newState: PlayerState, oldState: PlayerState)

}
