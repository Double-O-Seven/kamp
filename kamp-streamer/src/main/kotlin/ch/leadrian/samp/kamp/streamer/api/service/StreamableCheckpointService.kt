package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableCheckpoint
import ch.leadrian.samp.kamp.streamer.runtime.CheckpointStreamer
import javax.inject.Inject

class StreamableCheckpointService
@Inject
internal constructor(private val checkpointStreamer: CheckpointStreamer) {

    @JvmOverloads
    fun createStreamableCheckpoint(
            coordinates: Vector3D,
            size: Float,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            priority: Int = 0,
            streamDistance: Float = 300f
    ): StreamableCheckpoint = checkpointStreamer.createCheckpoint(
            coordinates = coordinates,
            size = size,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            priority = priority,
            streamDistance = streamDistance
    )

    @JvmOverloads
    fun createStreamableCheckpoint(
            coordinates: Vector3D,
            size: Float,
            interiorId: Int,
            virtualWorldId: Int,
            priority: Int = 0,
            streamDistance: Float = 300f
    ): StreamableCheckpoint = checkpointStreamer.createCheckpoint(
            coordinates = coordinates,
            size = size,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId),
            priority = priority,
            streamDistance = streamDistance
    )

    @JvmOverloads
    fun createStreamableCheckpoint(
            sphere: Sphere,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            priority: Int = 0,
            streamDistance: Float = 300f
    ): StreamableCheckpoint = checkpointStreamer.createCheckpoint(
            coordinates = vector3DOf(x = sphere.x, y = sphere.y, z = sphere.z),
            size = sphere.radius,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            priority = priority,
            streamDistance = streamDistance
    )

    @JvmOverloads
    fun createStreamableCheckpoint(
            sphere: Sphere,
            interiorId: Int,
            virtualWorldId: Int,
            priority: Int = 0,
            streamDistance: Float = 300f
    ): StreamableCheckpoint = checkpointStreamer.createCheckpoint(
            coordinates = vector3DOf(x = sphere.x, y = sphere.y, z = sphere.z),
            size = sphere.radius,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId),
            priority = priority,
            streamDistance = streamDistance
    )

}