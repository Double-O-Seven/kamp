package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject

interface OnPlayerObjectMovedListener {

    fun onPlayerObjectMoved(playerMapObject: PlayerMapObject)

}
