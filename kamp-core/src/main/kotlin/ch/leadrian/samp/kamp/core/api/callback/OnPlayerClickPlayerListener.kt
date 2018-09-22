package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerClickPlayerListener {

    fun onPlayerClickPlayer(player: Player, clickedPlayer: Player, source: ch.leadrian.samp.kamp.core.api.constants.ClickPlayerSource): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object Continue : Result(false)
    }


}
