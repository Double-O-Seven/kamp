package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerDamageStreamableActorHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerPickUpStreamablePickupHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.PlayerMapIconIdAllocator

internal class StreamerModule : AbstractStreamerModule() {

    override fun configure() {
        newCallbackListenerRegistrySetBinder().apply {
            addBinding().to(OnPlayerEnterStreamableAreaHandler::class.java)
            addBinding().to(OnPlayerLeaveStreamableAreaHandler::class.java)

            addBinding().to(OnPlayerSelectStreamableMapObjectHandler::class.java)
            addBinding().to(OnPlayerEditStreamableMapObjectHandler::class.java)
            addBinding().to(OnStreamableMapObjectMovedHandler::class.java)
            addBinding().to(OnStreamableMapObjectStreamInHandler::class.java)
            addBinding().to(OnStreamableMapObjectStreamOutHandler::class.java)

            addBinding().to(OnStreamableActorStreamInHandler::class.java)
            addBinding().to(OnStreamableActorStreamOutHandler::class.java)
            addBinding().to(OnPlayerDamageStreamableActorHandler::class.java)

            addBinding().to(OnStreamableTextLabelStreamInHandler::class.java)
            addBinding().to(OnStreamableTextLabelStreamOutHandler::class.java)

            addBinding().to(OnStreamablePickupStreamInHandler::class.java)
            addBinding().to(OnStreamablePickupStreamOutHandler::class.java)
            addBinding().to(OnPlayerPickUpStreamablePickupHandler::class.java)
        }
        newStreamerSetBinder().apply {
            addBinding().to(MapObjectStreamer::class.java)
            addBinding().to(TextLabelStreamer::class.java)
            addBinding().to(ActorStreamer::class.java)
            addBinding().to(PickupStreamer::class.java)
        }
        bind(StreamerExecutor::class.java).asEagerSingleton()
        bind(PlayerMapIconIdAllocator::class.java).asEagerSingleton()
    }

}