package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.PlayerMapObject

interface OnPlayerObjectMovedListener {

    fun onPlayerObjectMoved(playerMapObject: PlayerMapObject)

}
