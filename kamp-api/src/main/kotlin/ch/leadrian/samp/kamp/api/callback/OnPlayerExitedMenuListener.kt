package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerExitedMenuListener {

    fun onPlayerExitedMenu(player: Player)

}
