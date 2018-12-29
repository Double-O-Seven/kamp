package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.MapObject

interface OnMapObjectMovedListener {

    fun onMapObjectMoved(mapObject: MapObject)

}
