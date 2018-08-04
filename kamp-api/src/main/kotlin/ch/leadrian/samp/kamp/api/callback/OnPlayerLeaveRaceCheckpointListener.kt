package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerLeaveRaceCheckpointListener {

    fun onPlayerLeaveRaceCheckpoint(player: Player)

}
