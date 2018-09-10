package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerRequestSpawnListener {

    fun onPlayerRequestSpawn(player: Player): Result

    sealed class Result(val value: Boolean) {

        object Granted : Result(true)

        object Denied : Result(false)
    }

}
