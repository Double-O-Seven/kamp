package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerInteriorChangeListener {

    fun onPlayerInteriorChange(player: Player, newInteriorId: Int, oldInteriorId: Int)

}
