package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerInteriorChangeListener {

    fun onPlayerInteriorChange(player: Player, newInteriorId: Int, oldInteriorId: Int)

}
