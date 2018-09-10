package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerCommandTextListener {

    fun onPlayerCommandText(player: Player, commandText: String): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object UnknownCommand : Result(false)
    }

}
