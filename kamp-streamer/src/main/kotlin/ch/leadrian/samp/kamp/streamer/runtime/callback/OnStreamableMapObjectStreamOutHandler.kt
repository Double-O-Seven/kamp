package ch.leadrian.samp.kamp.streamer.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectStreamOutListener
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnStreamableMapObjectStreamOutHandler
@Inject
constructor() : CallbackListenerRegistry<OnStreamableMapObjectStreamOutListener>(OnStreamableMapObjectStreamOutListener::class), OnStreamableMapObjectStreamOutListener {

    override fun onStreamableMapObjectStreamOut(streamableMapObject: StreamableMapObject, forPlayer: Player) {
        listeners.forEach {
            it.onStreamableMapObjectStreamOut(streamableMapObject, forPlayer)
        }
    }

}
