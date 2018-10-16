package ch.leadrian.samp.kamp.streamer.callback

import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject

interface OnStreamableMapObjectMovedListener {

    fun onStreamableMapObjectMoved(streamableMapObject: StreamableMapObject)

}