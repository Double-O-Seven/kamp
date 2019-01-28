package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.service.PickupService
import ch.leadrian.samp.kamp.streamer.runtime.PickupStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerPickUpStreamablePickupHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamablePickupImpl
import javax.inject.Inject

internal class StreamablePickupFactory
@Inject
constructor(
        private val pickupService: PickupService,
        private val onStreamablePickupStreamInHandler: OnStreamablePickupStreamInHandler,
        private val onStreamablePickupStreamOutHandler: OnStreamablePickupStreamOutHandler,
        private val onPlayerPickUpStreamablePickupHandler: OnPlayerPickUpStreamablePickupHandler
) {

    fun create(
            modelId: Int,
            coordinates: Vector3D,
            type: Int,
            virtualWorldId: Int?,
            interiorIds: MutableSet<Int>,
            streamDistance: Float,
            priority: Int,
            pickupStreamer: PickupStreamer
    ): StreamablePickupImpl {
        return StreamablePickupImpl(
                modelId = modelId,
                coordinates = coordinates,
                type = type,
                virtualWorldId = virtualWorldId,
                interiorIds = interiorIds,
                streamDistance = streamDistance,
                priority = priority,
                pickupStreamer = pickupStreamer,
                pickupService = pickupService,
                onStreamablePickupStreamInHandler = onStreamablePickupStreamInHandler,
                onStreamablePickupStreamOutHandler = onStreamablePickupStreamOutHandler,
                onPlayerPickUpStreamablePickupHandler = onPlayerPickUpStreamablePickupHandler
        )
    }

}