package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutHandler

internal class StreamerModule : AbstractStreamerModule() {

    override fun configure() {
        newCallbackListenerRegistrySetBinder().apply {
            addBinding().to(OnStreamableMapObjectMovedHandler::class.java)
            addBinding().to(OnPlayerEditStreamableMapObjectHandler::class.java)
            addBinding().to(OnPlayerSelectStreamableMapObjectHandler::class.java)
            addBinding().to(OnStreamableMapObjectStreamInHandler::class.java)
            addBinding().to(OnStreamableMapObjectStreamOutHandler::class.java)
        }
        newStreamerSetBinder().apply {
            addBinding().to(MapObjectStreamer::class.java)
            addBinding().to(TextLabelStreamer::class.java)
        }
        bind(StreamerExecutor::class.java).asEagerSingleton()
    }

}