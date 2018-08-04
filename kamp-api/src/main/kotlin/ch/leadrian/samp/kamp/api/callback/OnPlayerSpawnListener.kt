package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerSpawnListener {

    fun onPlayerSpawn(player: Player): Boolean

}
