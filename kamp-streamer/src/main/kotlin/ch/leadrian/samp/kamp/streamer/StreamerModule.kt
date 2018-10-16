package ch.leadrian.samp.kamp.streamer

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler

internal class StreamerModule : KampModule() {

    override fun configure() {
        newCallbackListenerRegistry().apply {
            addBinding().to(OnStreamableMapObjectMovedHandler::class.java)
        }
    }

}