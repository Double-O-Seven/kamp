package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableActor
import ch.leadrian.samp.kamp.streamer.runtime.ActorStreamer
import javax.inject.Inject

class StreamableActorService
@Inject
internal constructor(private val actorStreamer: ActorStreamer) {

    @JvmOverloads
    fun createStreamableActor(
            model: SkinModel,
            coordinates: Vector3D,
            angle: Float,
            virtualWorldId: Int = 0,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            isInvulnerable: Boolean = true,
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamableActor = actorStreamer.createActor(
            model = model,
            coordinates = coordinates,
            angle = angle,
            isInvulnerable = isInvulnerable,
            virtualWorldId = virtualWorldId,
            interiorIds = interiorIds,
            streamDistance = streamDistance,
            priority = priority
    )

    @JvmOverloads
    fun createStreamableActor(
            model: SkinModel,
            position: Position,
            virtualWorldId: Int = 0,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            isInvulnerable: Boolean = true,
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamableActor = actorStreamer.createActor(
            model = model,
            coordinates = position,
            angle = position.angle,
            isInvulnerable = isInvulnerable,
            virtualWorldId = virtualWorldId,
            interiorIds = interiorIds,
            streamDistance = streamDistance,
            priority = priority
    )

    @JvmOverloads
    fun createStreamableActor(
            model: SkinModel,
            coordinates: Vector3D,
            angle: Float,
            virtualWorldId: Int,
            interiorId: Int,
            isInvulnerable: Boolean = true,
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamableActor = actorStreamer.createActor(
            model = model,
            coordinates = coordinates,
            angle = angle,
            isInvulnerable = isInvulnerable,
            virtualWorldId = virtualWorldId,
            interiorIds = mutableSetOf(interiorId),
            streamDistance = streamDistance,
            priority = priority
    )

    @JvmOverloads
    fun createStreamableActor(
            model: SkinModel,
            position: Position,
            virtualWorldId: Int,
            interiorId: Int,
            isInvulnerable: Boolean = true,
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamableActor = actorStreamer.createActor(
            model = model,
            coordinates = position,
            angle = position.angle,
            isInvulnerable = isInvulnerable,
            virtualWorldId = virtualWorldId,
            interiorIds = mutableSetOf(interiorId),
            streamDistance = streamDistance,
            priority = priority
    )

    @JvmOverloads
    fun createStreamableActor(
            model: SkinModel,
            angledLocation: AngledLocation,
            isInvulnerable: Boolean = true,
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamableActor = actorStreamer.createActor(
            model = model,
            coordinates = angledLocation,
            angle = angledLocation.angle,
            isInvulnerable = isInvulnerable,
            virtualWorldId = angledLocation.virtualWorldId,
            interiorIds = mutableSetOf(angledLocation.interiorId),
            streamDistance = streamDistance,
            priority = priority
    )

    @JvmOverloads
    fun createStreamableActor(
            model: SkinModel,
            location: Location,
            angle: Float,
            isInvulnerable: Boolean = true,
            streamDistance: Float = 300f,
            priority: Int = 0
    ): StreamableActor = actorStreamer.createActor(
            model = model,
            coordinates = location,
            angle = angle,
            isInvulnerable = isInvulnerable,
            virtualWorldId = location.virtualWorldId,
            interiorIds = mutableSetOf(location.interiorId),
            streamDistance = streamDistance,
            priority = priority
    )

    fun setMaxStreamedInActors(maxStreamedInActors: Int) {
        actorStreamer.capacity = maxStreamedInActors
    }

}