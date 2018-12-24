package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import java.util.stream.Stream
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MapObjectStreamer
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val distanceBasedPlayerStreamerFactory: DistanceBasedPlayerStreamerFactory,
        private val streamableMapObjectFactory: StreamableMapObjectFactory
) : Streamer, StreamInCandidateSupplier<StreamableMapObjectImpl> {

    /*
     * The spatial index and set of moving or attached objects may only be accessed during
     * streaming to avoid any race conditions and expensive synchronization.
     * It is less expensive to simple queue some indexing tasks and then execute them on the streaming thread.
     */
    private val spatialIndex = SpatialIndex3D<StreamableMapObjectImpl>()

    /*
     * Moving or attached map objects constantly change their location. We don't want to constantly update the spatial index.
     */
    private val movingOrAttachedMapObjects = HashSet<StreamableMapObjectImpl>()

    private lateinit var delegate: DistanceBasedPlayerStreamer<StreamableMapObjectImpl>

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @PostConstruct
    fun initialize() {
        delegate = distanceBasedPlayerStreamerFactory.create(
                maxCapacity = SAMPConstants.MAX_OBJECTS - 1,
                streamInCandidateSupplier = this
        )
        callbackListenerManager.register(delegate)
    }

    fun createMapObject(
            modelId: Int,
            priority: Int,
            streamDistance: Float,
            coordinates: Vector3D,
            rotation: Vector3D,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>
    ): StreamableMapObjectImpl {
        val streamableMapObject = streamableMapObjectFactory.create(
                modelId = modelId,
                priority = priority,
                streamDistance = streamDistance,
                coordinates = coordinates,
                rotation = rotation,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                mapObjectStreamer = this
        )
        add(streamableMapObject)
        return streamableMapObject
    }

    fun onBoundingBoxChange(streamableMapObject: StreamableMapObjectImpl) {
        updateSpatialIndex(streamableMapObject)
    }

    fun onStateChange(streamableMapObject: StreamableMapObjectImpl) {
        if (streamableMapObject.isMoving || streamableMapObject.isAttached) {
            enableNonIndexStreaming(streamableMapObject)
        }
    }

    private fun updateSpatialIndex(streamableMapObject: StreamableMapObjectImpl) {
        delegate.beforeStream {
            if (!movingOrAttachedMapObjects.contains(streamableMapObject)) {
                spatialIndex.update(streamableMapObject)
            }
        }
    }

    private fun enableNonIndexStreaming(streamableMapObject: StreamableMapObjectImpl) {
        delegate.beforeStream {
            if (movingOrAttachedMapObjects.add(streamableMapObject)) {
                spatialIndex.remove(streamableMapObject)
            }
        }
    }

    private fun add(streamableMapObject: StreamableMapObjectImpl) {
        callbackListenerManager.register(streamableMapObject)
        delegate.beforeStream { spatialIndex.add(streamableMapObject) }
        streamableMapObject.onDestroy { remove(this) }
    }

    private fun remove(streamableMapObject: StreamableMapObjectImpl) {
        callbackListenerManager.unregister(streamableMapObject)
        delegate.beforeStream {
            movingOrAttachedMapObjects -= streamableMapObject
            spatialIndex.remove(streamableMapObject)
        }
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }

    override fun getStreamInCandidates(streamLocation: StreamLocation): Stream<StreamableMapObjectImpl> =
            Stream.concat(
                    spatialIndex.getIntersections(streamLocation.location).stream(),
                    movingOrAttachedMapObjects.stream()
            )
}