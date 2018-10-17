package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.streamer.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject
import ch.leadrian.samp.kamp.streamer.entity.factory.StreamableMapObjectFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
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

    private val mapObjects = mutableSetOf<StreamableMapObject>()
    private val movingOrAttachedMapObjects = mutableSetOf<StreamableMapObject>()
    private val spatialIndex = SpatialIndex3D<StreamableMapObject>()

    fun createMapObject(
            modelId: Int,
            priority: Int,
            streamDistance: Float,
            coordinates: Vector3D,
            rotation: Vector3D,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>
    ): StreamableMapObject {
        val mapObject = streamableMapObjectFactory.create(
                modelId = modelId,
                priority = priority,
                streamDistance = streamDistance,
                coordinates = coordinates,
                rotation = rotation,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds
        )
        mapObjects += mapObject
        spatialIndex.add(mapObject)
        mapObject.onBoundingBoxChanged {
            if (!movingOrAttachedMapObjects.contains(this)) {
                spatialIndex.update(this)
            }
        }
        mapObject.onDestroy {
            mapObjects -= this
            movingOrAttachedMapObjects -= this
            spatialIndex.remove(this)
        }
        mapObject.onAttach { enableNonIndexStreaming(this) }
        mapObject.onStartMoving { enableNonIndexStreaming(this) }
        return mapObject
    }

    private fun enableNonIndexStreaming(streamableMapObject: StreamableMapObject) {
        if (movingOrAttachedMapObjects.add(streamableMapObject)) {
            spatialIndex.remove(streamableMapObject)
        }
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        mapObjects.forEach { it.onPlayerDisconnect(player, reason) }
        super.onPlayerDisconnect(player, reason)
    }

    override fun getStreamInCandidates(streamLocation: StreamLocation): Stream<StreamableMapObject> =
            Stream.concat(
                    spatialIndex.getIntersections(streamLocation.location).stream(),
                    movingOrAttachedMapObjects.stream()
            )
}