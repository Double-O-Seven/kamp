package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.ClickPlayerSource
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerClickPlayerListener {

    fun onPlayerClickPlayer(player: Player, clickedPlayer: Player, source: ClickPlayerSource): Boolean

}
