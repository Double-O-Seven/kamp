package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.OnDestroyListener
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapIconImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapIconFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.full.safeCast

@Singleton
internal class MapIconStreamer
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val coordinatesBasedPlayerStreamerFactory: CoordinatesBasedPlayerStreamerFactory,
        private val streamableMapIconFactory: StreamableMapIconFactory
) : Streamer, OnDestroyListener {

    private lateinit var delegate: CoordinatesBasedPlayerStreamer<StreamableMapIconImpl, Rect3d>

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @PostConstruct
    fun initialize() {
        delegate = coordinatesBasedPlayerStreamerFactory.create(SpatialIndex3D(), 100)
        callbackListenerManager.register(delegate)
    }

    fun createMapIcon(
            coordinates: Vector3D,
            type: MapIconType,
            color: Color,
            style: MapIconStyle,
            priority: Int,
            streamDistance: Float,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>
    ): StreamableMapIconImpl {
        val streamableMapIcon = streamableMapIconFactory.create(
                coordinates = coordinates,
                type = type,
                color = color,
                style = style,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                priority = priority,
                streamDistance = streamDistance,
                mapIconStreamer = this
        )
        delegate.add(streamableMapIcon)
        callbackListenerManager.registerOnlyAs<OnPlayerDisconnectListener>(streamableMapIcon)
        streamableMapIcon.addOnDestroyListener(this)
        return streamableMapIcon
    }

    fun onBoundingBoxChange(streamableMapIcon: StreamableMapIconImpl) {
        delegate.onBoundingBoxChange(streamableMapIcon)
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }

    override fun onDestroy(destroyable: Destroyable) {
        StreamableMapIconImpl::class.safeCast(destroyable)?.let {
            callbackListenerManager.unregisterOnlyAs<OnPlayerDisconnectListener>(it)
        }
    }
}