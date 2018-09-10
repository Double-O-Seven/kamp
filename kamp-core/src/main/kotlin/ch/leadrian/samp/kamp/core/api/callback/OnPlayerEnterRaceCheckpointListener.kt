package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerEnterRaceCheckpointListener {

    fun onPlayerEnterRaceCheckpoint(player: Player)

}
