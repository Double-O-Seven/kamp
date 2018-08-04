package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.PlayerTextDraw

interface OnPlayerClickPlayerTextDrawListener {

    fun onPlayerClickPlayerTextDraw(textDraw: PlayerTextDraw): Boolean

}
