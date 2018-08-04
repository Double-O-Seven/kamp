package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerTextListener {

    fun onPlayerText(player: Player, text: String): Boolean

}
