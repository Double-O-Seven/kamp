package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerCommandTextListener {

    fun onPlayerCommandText(player: Player, commandText: String): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object UnknownCommand : Result(false)
    }

}
