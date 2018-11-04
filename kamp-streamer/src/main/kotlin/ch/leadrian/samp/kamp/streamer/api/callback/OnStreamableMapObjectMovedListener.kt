package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObject

interface OnStreamableMapObjectMovedListener {

    fun onStreamableMapObjectMoved(streamableMapObject: StreamableMapObject)

}