package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.service.ActorService
import ch.leadrian.samp.kamp.streamer.runtime.ActorStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerDamageStreamableActorHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableActorImpl
import javax.inject.Inject

internal class StreamableActorFactory
@Inject
constructor(
        private val actorService: ActorService,
        private val onStreamableActorStreamInHandler: OnStreamableActorStreamInHandler,
        private val onStreamableActorStreamOutHandler: OnStreamableActorStreamOutHandler,
        private val onPlayerDamageStreamableActorHandler: OnPlayerDamageStreamableActorHandler
) {

    fun create(
            model: SkinModel,
            coordinates: Vector3D,
            angle: Float,
            isInvulnerable: Boolean,
            virtualWorldId: Int,
            interiorIds: MutableSet<Int>,
            streamDistance: Float,
            priority: Int,
            actorStreamer: ActorStreamer
    ): StreamableActorImpl {
        return StreamableActorImpl(
                model = model,
                coordinates = coordinates,
                angle = angle,
                isInvulnerable = isInvulnerable,
                virtualWorldId = virtualWorldId,
                interiorIds = interiorIds,
                streamDistance = streamDistance,
                priority = priority,
                actorService = actorService,
                onStreamableActorStreamInHandler = onStreamableActorStreamInHandler,
                onStreamableActorStreamOutHandler = onStreamableActorStreamOutHandler,
                onPlayerDamageStreamableActorHandler = onPlayerDamageStreamableActorHandler,
                actorStreamer = actorStreamer
        )
    }

}