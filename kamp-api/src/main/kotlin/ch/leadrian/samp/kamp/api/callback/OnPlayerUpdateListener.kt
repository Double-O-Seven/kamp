package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerUpdateListener {

    fun onPlayerUpdate(player: Player): Boolean

}
