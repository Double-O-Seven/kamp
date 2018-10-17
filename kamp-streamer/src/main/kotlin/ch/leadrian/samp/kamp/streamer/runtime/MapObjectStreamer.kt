package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.streamer.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject
import ch.leadrian.samp.kamp.streamer.entity.factory.StreamableMapObjectFactory
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MapObjectStreamer
@Inject
constructor(
        asyncExecutor: AsyncExecutor,
        playerService: PlayerService,
        private val streamableMapObjectFactory: StreamableMapObjectFactory
) : DistanceBasedPlayerStreamer<StreamableMapObject>(
        SAMPConstants.MAX_OBJECTS - 100,
        asyncExecutor,
        playerService
) {

    private val streamableMapObjects = mutableSetOf<StreamableMapObject>()

    fun createMapObject(
            modelId: Int,
            priority: Int,
            streamDistance: Float,
            coordinates: Vector3D,
            rotation: Vector3D,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>
    ): StreamableMapObject {
        val streamableMapObject = streamableMapObjectFactory.create(
                modelId = modelId,
                priority = priority,
                streamDistance = streamDistance,
                coordinates = coordinates,
                rotation = rotation,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds
        )
        streamableMapObjects += streamableMapObject
        streamableMapObject.onDestroy {
            streamableMapObjects -= this
        }
        return streamableMapObject
    }

    override fun getStreamInCandidates(streamLocation: StreamLocation): Stream<StreamableMapObject> = streamableMapObjects.stream()
}