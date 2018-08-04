package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerStreamOutListener {

    fun onPlayerStreamOut(player: Player, forPlayer: Player)

}
