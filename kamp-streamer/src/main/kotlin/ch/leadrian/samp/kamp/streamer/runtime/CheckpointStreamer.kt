package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableCheckpointImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableCheckpointFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CheckpointStreamer
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val coordinatesBasedPlayerStreamerFactory: CoordinatesBasedPlayerStreamerFactory,
        private val streamableCheckpointFactory: StreamableCheckpointFactory
) : Streamer {

    private lateinit var delegate: CoordinatesBasedPlayerStreamer<StreamableCheckpointImpl, Rect3d>

    @PostConstruct
    fun initialize() {
        delegate = coordinatesBasedPlayerStreamerFactory.create(SpatialIndex3D(), 1)
        callbackListenerManager.register(delegate)
    }

    fun createCheckpoint(
            coordinates: Vector3D,
            size: Float,
            priority: Int,
            streamDistance: Float,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>
    ): StreamableCheckpointImpl {
        val streamableCheckpoint = streamableCheckpointFactory.create(
                coordinates = coordinates,
                size = size,
                priority = priority,
                streamDistance = streamDistance,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                checkpointStreamer = this
        )
        delegate.add(streamableCheckpoint)
        return streamableCheckpoint
    }

    fun onBoundingBoxChange(streamableCheckpoint: StreamableCheckpointImpl) {
        delegate.onBoundingBoxChange(streamableCheckpoint)
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }

}