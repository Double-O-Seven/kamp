package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableActorImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableActorFactory
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex3D
import com.conversantmedia.util.collection.geometry.Rect3d
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ActorStreamer
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val coordinatesBasedGlobalStreamerFactory: CoordinatesBasedGlobalStreamerFactory,
        private val streamableActorFactory: StreamableActorFactory
) : Streamer {

    private lateinit var delegate: CoordinatesBasedGlobalStreamer<StreamableActorImpl, Rect3d>

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @PostConstruct
    fun initialize() {
        delegate = coordinatesBasedGlobalStreamerFactory.create(SpatialIndex3D(), SAMPConstants.MAX_ACTORS)
        callbackListenerManager.register(delegate)
    }

    fun createActor(
            model: SkinModel,
            coordinates: Vector3D,
            angle: Float,
            isInvulnerable: Boolean,
            virtualWorldId: Int,
            interiorIds: MutableSet<Int>,
            streamDistance: Float,
            priority: Int
    ): StreamableActorImpl {
        val streamableActor = streamableActorFactory.create(
                model = model,
                coordinates = coordinates,
                angle = angle,
                isInvulnerable = isInvulnerable,
                virtualWorldId = virtualWorldId,
                interiorIds = interiorIds,
                streamDistance = streamDistance,
                priority = priority,
                actorStreamer = this
        )
        delegate.add(streamableActor)
        return streamableActor
    }

    fun onBoundingBoxChange(streamableActor: StreamableActorImpl) {
        delegate.onBoundingBoxChange(streamableActor)
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }
}