package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass

interface OnPlayerRequestClassListener {

    fun onPlayerRequestClass(player: Player, playerClass: PlayerClass)

}
