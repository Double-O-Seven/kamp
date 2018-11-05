package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject

interface OnStreamableMapObjectStreamInListener {

    fun onStreamableMapObjectStreamIn(streamableMapObject: StreamableMapObject, forPlayer: Player)

}
