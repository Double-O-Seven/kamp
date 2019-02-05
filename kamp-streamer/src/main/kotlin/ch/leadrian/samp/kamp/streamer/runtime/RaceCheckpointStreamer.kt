package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableRaceCheckpointImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableRaceCheckpointFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RaceCheckpointStreamer
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val coordinatesBasedPlayerStreamerFactory: CoordinatesBasedPlayerStreamerFactory,
        private val streamableRaceCheckpointFactory: StreamableRaceCheckpointFactory
) : Streamer {

    private lateinit var delegate: CoordinatesBasedPlayerStreamer<StreamableRaceCheckpointImpl, Rect3d>

    @PostConstruct
    fun initialize() {
        delegate = coordinatesBasedPlayerStreamerFactory.create(SpatialIndex3D(), 1)
        callbackListenerManager.register(delegate)
    }

    fun createRaceCheckpoint(
            coordinates: Vector3D,
            size: Float,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D?,
            priority: Int,
            streamDistance: Float,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>
    ): StreamableRaceCheckpointImpl {
        val streamableRaceCheckpoint = streamableRaceCheckpointFactory.create(
                coordinates = coordinates,
                size = size,
                type = type,
                nextCoordinates = nextCoordinates,
                priority = priority,
                streamDistance = streamDistance,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                raceCheckpointStreamer = this
        )
        delegate.add(streamableRaceCheckpoint)
        return streamableRaceCheckpoint
    }

    fun onBoundingBoxChange(streamableRaceCheckpoint: StreamableRaceCheckpointImpl) {
        delegate.onBoundingBoxChange(streamableRaceCheckpoint)
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }

}