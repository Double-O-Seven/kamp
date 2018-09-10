package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextDraw

interface OnPlayerClickTextDrawListener {

    fun onPlayerClickTextDraw(player: Player, textDraw: TextDraw): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object NotFound : Result(false)
    }

}
