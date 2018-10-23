package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector3D
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
        callbackListenerManager: CallbackListenerManager,
        private val streamableMapObjectFactory: StreamableMapObjectFactory
) : DistanceBasedPlayerStreamer<StreamableMapObject>(
        asyncExecutor = asyncExecutor,
        playerService = playerService,
        callbackListenerManager = callbackListenerManager,
        maxCapacity = SAMPConstants.MAX_OBJECTS
) {

    /*
     * The spatial index and set of createMoving or attached objects may only be accessed during
     * streaming to avoid any race conditions and expensive synchronization.
     * It is less expensive to simple queue some indexing tasks and then execute them on the streaming thread.
     */
    private val spatialIndex = SpatialIndex3D<StreamableMapObject>()
    /*
     * Moving or attached map objects constantly change their location. We don't want to constantly update the spatial index.
     */
    private val movingOrAttachedMapObjects = HashSet<StreamableMapObject>()

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
        add(streamableMapObject)
        registerCallbackHandlers(streamableMapObject)
        return streamableMapObject
    }

    private fun registerCallbackHandlers(streamableMapObject: StreamableMapObject) {
        streamableMapObject.onDestroy { remove(this) }
        streamableMapObject.onBoundingBoxChanged { updateSpatialIndex(this) }
        streamableMapObject.onStateChange { _, _ ->
            if (isMoving || isAttached) {
                enableNonIndexStreaming(this)
            }
        }
    }

    private fun add(streamableMapObject: StreamableMapObject) {
        callbackListenerManager.register(streamableMapObject)
        onStream { spatialIndex.add(streamableMapObject) }
    }

    private fun remove(streamableMapObject: StreamableMapObject) {
        callbackListenerManager.unregister(streamableMapObject)
        onStream {
            movingOrAttachedMapObjects -= streamableMapObject
            spatialIndex.remove(streamableMapObject)
        }
    }

    private fun updateSpatialIndex(streamableMapObject: StreamableMapObject) {
        onStream {
            if (!movingOrAttachedMapObjects.contains(streamableMapObject)) {
                spatialIndex.update(streamableMapObject)
            }
        }
    }

    private fun enableNonIndexStreaming(streamableMapObject: StreamableMapObject) {
        onStream {
            if (movingOrAttachedMapObjects.add(streamableMapObject)) {
                spatialIndex.remove(streamableMapObject)
            }
        }
    }

    override fun getStreamInCandidates(streamLocation: StreamLocation): Stream<StreamableMapObject> =
            Stream.concat(
                    spatialIndex.getIntersections(streamLocation.location).stream(),
                    movingOrAttachedMapObjects.stream()
            )
}