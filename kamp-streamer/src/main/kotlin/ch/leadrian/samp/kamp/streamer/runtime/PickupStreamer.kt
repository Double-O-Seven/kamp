package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamablePickupImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamablePickupFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PickupStreamer
@Inject
constructor(
        private val coordinatesBasedGlobalStreamerFactory: CoordinatesBasedGlobalStreamerFactory,
        private val streamablePickupFactory: StreamablePickupFactory
) : Streamer {

    private lateinit var delegate: CoordinatesBasedGlobalStreamer<StreamablePickupImpl, Rect3d>

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @PostConstruct
    fun initialize() {
        delegate = coordinatesBasedGlobalStreamerFactory.create(SpatialIndex3D(), SAMPConstants.MAX_PICKUPS)
    }

    fun createPickup(
            modelId: Int,
            coordinates: Vector3D,
            type: Int,
            virtualWorldId: Int?,
            interiorIds: MutableSet<Int>,
            streamDistance: Float,
            priority: Int
    ): StreamablePickupImpl {
        val streamablePickup = streamablePickupFactory.create(
                modelId = modelId,
                coordinates = coordinates,
                type = type,
                virtualWorldId = virtualWorldId,
                interiorIds = interiorIds,
                streamDistance = streamDistance,
                priority = priority,
                pickupStreamer = this
        )
        delegate.add(streamablePickup)
        return streamablePickup
    }

    fun onBoundingBoxChange(streamablePickup: StreamablePickupImpl) {
        delegate.onBoundingBoxChange(streamablePickup)
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }
}