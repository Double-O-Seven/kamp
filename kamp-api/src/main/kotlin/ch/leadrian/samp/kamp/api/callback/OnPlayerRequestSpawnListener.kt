package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerRequestSpawnListener {

    fun onPlayerRequestSpawn(player: Player): Boolean

}
