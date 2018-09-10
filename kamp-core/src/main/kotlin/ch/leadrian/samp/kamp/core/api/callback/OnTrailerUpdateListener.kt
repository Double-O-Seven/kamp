package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnTrailerUpdateListener {

    fun onTrailerUpdate(player: Player, vehicle: Vehicle): Result

    sealed class Result(val value: Boolean) {

        object Sync : Result(true)

        object Desync : Result(false)
    }

}
