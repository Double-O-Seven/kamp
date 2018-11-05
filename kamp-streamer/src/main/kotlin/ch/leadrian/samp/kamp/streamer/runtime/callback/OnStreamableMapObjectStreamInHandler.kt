package ch.leadrian.samp.kamp.streamer.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectStreamInListener
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnStreamableMapObjectStreamInHandler
@Inject
constructor() : CallbackListenerRegistry<OnStreamableMapObjectStreamInListener>(OnStreamableMapObjectStreamInListener::class), OnStreamableMapObjectStreamInListener {

    override fun onStreamableMapObjectStreamIn(streamableMapObject: StreamableMapObject, forPlayer: Player) {
        listeners.forEach {
            it.onStreamableMapObjectStreamIn(streamableMapObject, forPlayer)
        }
    }

}
