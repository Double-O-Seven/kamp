package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerEnterRaceCheckpointListener {

    fun onPlayerEnterRaceCheckpoint(player: Player)

}
