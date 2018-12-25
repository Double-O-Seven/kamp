package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MapObjectStreamer
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val coordinatesBasedPlayerStreamerFactory: CoordinatesBasedPlayerStreamerFactory,
        private val streamableMapObjectFactory: StreamableMapObjectFactory
) : Streamer {

    private lateinit var delegate: CoordinatesBasedPlayerStreamer<StreamableMapObjectImpl, Rect3d>

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @PostConstruct
    fun initialize() {
        delegate = coordinatesBasedPlayerStreamerFactory.create(SpatialIndex3D(), SAMPConstants.MAX_OBJECTS - 1)
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
        delegate.add(streamableMapObject)
        callbackListenerManager.register(streamableMapObject)
        return streamableMapObject
    }

    fun onBoundingBoxChange(streamableMapObject: StreamableMapObjectImpl) {
        delegate.onBoundingBoxChange(streamableMapObject)
    }

    fun onStateChange(streamableMapObject: StreamableMapObjectImpl) {
        if (streamableMapObject.isMoving || streamableMapObject.isAttached) {
            delegate.removeFromSpatialIndex(streamableMapObject)
        }
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }
}