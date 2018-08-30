package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerRequestSpawnListener {

    fun onPlayerRequestSpawn(player: Player): Result

    sealed class Result(val value: Boolean) {

        object Granted : Result(true)

        object Denied : Result(false)
    }

}
