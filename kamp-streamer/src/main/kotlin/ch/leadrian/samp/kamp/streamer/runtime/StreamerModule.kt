package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.streamer.api.AbstractStreamerModule
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.api.service.StreamerService
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectFactory
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectStateFactory
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectStateMachineFactory
import ch.leadrian.samp.kamp.streamer.runtime.util.TimeProvider

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