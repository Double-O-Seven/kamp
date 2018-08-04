package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.TextDraw

interface OnPlayerClickTextDrawListener {

    fun onPlayerClickTextDraw(player: Player, textDraw: TextDraw): Boolean

}
