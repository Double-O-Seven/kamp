package ch.leadrian.samp.kamp.streamer

import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.entity.factory.StreamableMapObjectFactory
import ch.leadrian.samp.kamp.streamer.runtime.MapObjectStreamer
import ch.leadrian.samp.kamp.streamer.runtime.StreamerExecutor
import ch.leadrian.samp.kamp.streamer.service.StreamerService

internal class StreamerModule : AbstractStreamerModule() {

    override fun configure() {
        newCallbackListenerRegistrySetBinder().apply {
            addBinding().to(OnStreamableMapObjectMovedHandler::class.java)
        }
        newStreamerSetBinder().apply {
            addBinding().to(MapObjectStreamer::class.java)
        }
        bind(StreamerExecutor::class.java).asEagerSingleton()
        bind(StreamerService::class.java)
        bind(StreamableMapObjectFactory::class.java)
    }

}