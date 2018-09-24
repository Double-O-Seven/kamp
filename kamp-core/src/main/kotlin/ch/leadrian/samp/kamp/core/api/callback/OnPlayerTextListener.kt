package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerTextListener {

    fun onPlayerText(player: Player, text: String): Result

    sealed class Result(val value: Boolean) {

        object Allowed : Result(true)

        object Blocked : Result(false)
    }

}
