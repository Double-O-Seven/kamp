package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerStreamInListener {

    fun onPlayerStreamIn(player: Player, forPlayer: Player)

}
