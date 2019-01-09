package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import java.util.Locale
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TextLabelStreamer
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val coordinatesBasedPlayerStreamerFactory: CoordinatesBasedPlayerStreamerFactory,
        private val streamableTextLabelFactory: StreamableTextLabelFactory
) : Streamer {

    private lateinit var delegate: CoordinatesBasedPlayerStreamer<StreamableTextLabelImpl, Rect3d>

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @PostConstruct
    fun initialize() {
        delegate = coordinatesBasedPlayerStreamerFactory.create(SpatialIndex3D(), SAMPConstants.MAX_3DTEXT_PLAYER)
        callbackListenerManager.register(delegate)
    }

    fun createTextLabel(
            coordinates: Vector3D,
            textSupplier: (Locale) -> String,
            color: Color,
            streamDistance: Float,
            priority: Int,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>,
            testLOS: Boolean
    ): StreamableTextLabelImpl {
        val streamableTextLabel = streamableTextLabelFactory.create(
                coordinates = coordinates,
                textSupplier = textSupplier,
                color = color,
                streamDistance = streamDistance,
                priority = priority,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                testLOS = testLOS,
                textLabelStreamer = this
        )
        delegate.add(streamableTextLabel)
        callbackListenerManager.register(streamableTextLabel)
        return streamableTextLabel
    }

    fun onBoundingBoxChange(streamableTextLabel: StreamableTextLabelImpl) {
        delegate.onBoundingBoxChange(streamableTextLabel)
    }

    fun onStateChange(streamableTextLabel: StreamableTextLabelImpl) {
        if (streamableTextLabel.isAttached) {
            delegate.removeFromSpatialIndex(streamableTextLabel)
        }
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }
}