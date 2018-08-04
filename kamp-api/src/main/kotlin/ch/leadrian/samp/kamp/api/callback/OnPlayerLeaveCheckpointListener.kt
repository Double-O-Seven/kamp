package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerLeaveCheckpointListener {

    fun onPlayerLeaveCheckpoint(player: Player)

}
