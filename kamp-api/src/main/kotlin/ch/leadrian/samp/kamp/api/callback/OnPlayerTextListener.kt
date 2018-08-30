package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerTextListener {

    fun onPlayerText(player: Player, text: String): Result

    sealed class Result(val value: Boolean) {

        object Allow : Result(true)

        object Block : Result(false)
    }

}
