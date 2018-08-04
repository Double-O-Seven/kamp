package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.MenuRow
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerSelectedMenuRowListener {

    fun onPlayerSelectedMenuRow(player: Player, menuRow: MenuRow)

}
