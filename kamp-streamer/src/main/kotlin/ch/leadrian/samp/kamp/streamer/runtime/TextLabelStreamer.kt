package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableTextLabelImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableTextLabelFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import java.util.stream.Stream
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TextLabelStreamer
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val distanceBasedPlayerStreamerFactory: DistanceBasedPlayerStreamerFactory,
        private val streamableTextLabelFactory: StreamableTextLabelFactory
) : Streamer, StreamInCandidateSupplier<StreamableTextLabelImpl> {

    /*
     * The spatial index and set of attached text labels may only be accessed during
     * streaming to avoid any race conditions and expensive synchronization.
     * It is less expensive to simple queue some indexing tasks and then execute them on the streaming thread.
     */
    private val spatialIndex = SpatialIndex3D<StreamableTextLabelImpl>()

    /*
     * Moving or attached map objects constantly change their location. We don't want to constantly update the spatial index.
     */
    private val attachedTextLabels = HashSet<StreamableTextLabelImpl>()

    private lateinit var delegate: DistanceBasedPlayerStreamer<StreamableTextLabelImpl>

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @PostConstruct
    fun initialize() {
        delegate = distanceBasedPlayerStreamerFactory.create(
                maxCapacity = SAMPConstants.MAX_3DTEXT_PLAYER - 1,
                streamInCandidateSupplier = this
        )
        callbackListenerManager.register(delegate)
    }

    fun createTextLabel(
            coordinates: Vector3D,
            text: String,
            color: Color,
            streamDistance: Float,
            priority: Int,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>,
            testLOS: Boolean
    ): StreamableTextLabelImpl {
        val streamableTextLabel = streamableTextLabelFactory.create(
                coordinates = coordinates,
                text = text,
                color = color,
                streamDistance = streamDistance,
                priority = priority,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                testLOS = testLOS,
                textLabelStreamer = this
        )
        add(streamableTextLabel)
        return streamableTextLabel
    }

    fun onBoundingBoxChange(streamableTextLabel: StreamableTextLabelImpl) {
        updateSpatialIndex(streamableTextLabel)
    }

    fun onStateChange(streamableTextLabel: StreamableTextLabelImpl) {
        if (streamableTextLabel.isAttached) {
            enableNonIndexStreaming(streamableTextLabel)
        }
    }

    private fun updateSpatialIndex(streamableTextLabel: StreamableTextLabelImpl) {
        delegate.beforeStream {
            if (!attachedTextLabels.contains(streamableTextLabel)) {
                spatialIndex.update(streamableTextLabel)
            }
        }
    }

    private fun enableNonIndexStreaming(streamableTextLabel: StreamableTextLabelImpl) {
        delegate.beforeStream {
            if (attachedTextLabels.add(streamableTextLabel)) {
                spatialIndex.remove(streamableTextLabel)
            }
        }
    }

    private fun add(streamableTextLabel: StreamableTextLabelImpl) {
        callbackListenerManager.register(streamableTextLabel)
        delegate.beforeStream { spatialIndex.add(streamableTextLabel) }
        streamableTextLabel.onDestroy { remove(this) }
    }

    private fun remove(streamableTextLabel: StreamableTextLabelImpl) {
        callbackListenerManager.unregister(streamableTextLabel)
        delegate.beforeStream {
            attachedTextLabels -= streamableTextLabel
            spatialIndex.remove(streamableTextLabel)
        }
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }

    override fun getStreamInCandidates(streamLocation: StreamLocation): Stream<StreamableTextLabelImpl> =
            Stream.concat(
                    spatialIndex.getIntersections(streamLocation.location).stream(),
                    attachedTextLabels.stream()
            )
}