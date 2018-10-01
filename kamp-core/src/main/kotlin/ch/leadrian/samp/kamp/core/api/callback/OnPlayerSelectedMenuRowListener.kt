package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerSelectedMenuRowListener {

    fun onPlayerSelectedMenuRow(player: Player, rowIndex: Int)

}
