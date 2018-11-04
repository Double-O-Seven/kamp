package ch.leadrian.samp.kamp.streamer.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectMovedListener
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnStreamableMapObjectMovedHandler
@Inject
constructor() : CallbackListenerRegistry<OnStreamableMapObjectMovedListener>(OnStreamableMapObjectMovedListener::class), OnStreamableMapObjectMovedListener {

    override fun onStreamableMapObjectMoved(streamableMapObject: StreamableMapObject) {
        listeners.forEach { it.onStreamableMapObjectMoved(streamableMapObject) }
    }

}