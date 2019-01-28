package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.api.entity.StreamablePickup
import ch.leadrian.samp.kamp.streamer.runtime.PickupStreamer
import javax.inject.Inject

class StreamablePickupService
@Inject
internal constructor(private val pickupStreamer: PickupStreamer) {

    @JvmOverloads
    fun createStreamablePickup(
            modelId: Int,
            coordinates: Vector3D,
            type: Int,
            virtualWorldId: Int = 0,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamablePickup = pickupStreamer.createPickup(
            modelId = modelId,
            coordinates = coordinates,
            type = type,
            virtualWorldId = virtualWorldId,
            interiorIds = interiorIds,
            streamDistance = streamDistance,
            priority = priority
    )

    @JvmOverloads
    fun createStreamablePickup(
            modelId: Int,
            coordinates: Vector3D,
            type: Int,
            virtualWorldId: Int,
            interiorId: Int,
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamablePickup = pickupStreamer.createPickup(
            modelId = modelId,
            coordinates = coordinates,
            type = type,
            virtualWorldId = virtualWorldId,
            interiorIds = mutableSetOf(interiorId),
            streamDistance = streamDistance,
            priority = priority
    )

    @JvmOverloads
    fun createStreamablePickup(
            modelId: Int,
            location: Location,
            type: Int,
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamablePickup = pickupStreamer.createPickup(
            modelId = modelId,
            coordinates = location,
            type = type,
            virtualWorldId = location.virtualWorldId,
            interiorIds = mutableSetOf(location.interiorId),
            streamDistance = streamDistance,
            priority = priority
    )

    fun setMaxStreamedInPickups(maxStreamedInPickups: Int) {
        pickupStreamer.capacity = maxStreamedInPickups
    }

}