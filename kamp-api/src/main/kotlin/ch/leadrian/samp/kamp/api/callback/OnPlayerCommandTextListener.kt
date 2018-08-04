package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerCommandTextListener {

    fun onPlayerCommandText(player: Player, commandText: String): Boolean

}
