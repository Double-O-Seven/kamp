package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.PlayerState
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerStateChangeListener {

    fun onPlayerStateChange(player: Player, newState: PlayerState, oldState: PlayerState)

}
