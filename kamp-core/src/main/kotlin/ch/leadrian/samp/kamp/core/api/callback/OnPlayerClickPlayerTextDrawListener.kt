package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw

interface OnPlayerClickPlayerTextDrawListener {

    fun onPlayerClickPlayerTextDraw(textDraw: PlayerTextDraw): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object NotFound : Result(false)
    }

}
