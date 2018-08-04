package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerStreamInListener {

    fun onPlayerStreamIn(player: Player, forPlayer: Player)

}
