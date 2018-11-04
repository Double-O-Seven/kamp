package ch.leadrian.samp.kamp.streamer

import ch.leadrian.samp.kamp.streamer.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObjectStateFactory
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObjectStateMachineFactory
import ch.leadrian.samp.kamp.streamer.entity.factory.StreamableMapObjectFactory
import ch.leadrian.samp.kamp.streamer.runtime.MapObjectStreamer
import ch.leadrian.samp.kamp.streamer.runtime.StreamerExecutor
import ch.leadrian.samp.kamp.streamer.service.StreamerService
import ch.leadrian.samp.kamp.streamer.util.TimeProvider

internal class StreamerModule : AbstractStreamerModule() {

    override fun configure() {
        bind(OnStreamableMapObjectMovedHandler::class.java)
        bind(OnPlayerEditStreamableMapObjectHandler::class.java)
        bind(OnPlayerSelectStreamableMapObjectHandler::class.java)
        newCallbackListenerRegistrySetBinder().apply {
            addBinding().to(OnStreamableMapObjectMovedHandler::class.java)
            addBinding().to(OnPlayerEditStreamableMapObjectHandler::class.java)
            addBinding().to(OnPlayerSelectStreamableMapObjectHandler::class.java)
        }
        bind(MapObjectStreamer::class.java)
        newStreamerSetBinder().apply {
            addBinding().to(MapObjectStreamer::class.java)
        }
        bind(StreamerExecutor::class.java).asEagerSingleton()
        bind(StreamerService::class.java)
        bind(StreamableMapObjectFactory::class.java)
        bind(StreamableMapObjectStateFactory::class.java)
        bind(StreamableMapObjectStateMachineFactory::class.java)
        bind(TimeProvider::class.java)
    }

}