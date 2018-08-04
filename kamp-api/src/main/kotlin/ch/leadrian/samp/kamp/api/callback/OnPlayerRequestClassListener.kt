package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.PlayerClass

interface OnPlayerRequestClassListener {

    fun onPlayerRequestClass(player: Player, playerClass: PlayerClass)

}
