package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.MapObject

interface OnObjectMovedListener {

    fun onObjectMoved(mapObject: MapObject)

}
