package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerUpdateListener {

    fun onPlayerUpdate(player: Player): Result

    sealed class Result(val value: Boolean) {

        object Sync : Result(true)

        object Desync : Result(false)
    }

}
