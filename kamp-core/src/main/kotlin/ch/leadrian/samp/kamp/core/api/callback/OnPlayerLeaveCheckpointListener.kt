package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerLeaveCheckpointListener {

    fun onPlayerLeaveCheckpoint(player: Player)

}
