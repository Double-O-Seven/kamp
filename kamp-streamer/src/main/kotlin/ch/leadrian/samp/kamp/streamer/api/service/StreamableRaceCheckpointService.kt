package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableRaceCheckpoint
import ch.leadrian.samp.kamp.streamer.runtime.RaceCheckpointStreamer
import javax.inject.Inject

class StreamableRaceCheckpointService
@Inject
internal constructor(private val raceCheckpointStreamer: RaceCheckpointStreamer) {

    @JvmOverloads
    fun createStreamableRaceCheckpoint(
            coordinates: Vector3D,
            size: Float,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D? = null,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            priority: Int = 0,
            streamDistance: Float = 20f
    ): StreamableRaceCheckpoint = raceCheckpointStreamer.createRaceCheckpoint(
            coordinates = coordinates,
            size = size,
            type = type,
            nextCoordinates = nextCoordinates,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            priority = priority,
            streamDistance = streamDistance
    )

    @JvmOverloads
    fun createStreamableRaceCheckpoint(
            coordinates: Vector3D,
            size: Float,
            type: RaceCheckpointType,
            interiorId: Int,
            virtualWorldId: Int,
            nextCoordinates: Vector3D? = null,
            priority: Int = 0,
            streamDistance: Float = 20f
    ): StreamableRaceCheckpoint = raceCheckpointStreamer.createRaceCheckpoint(
            coordinates = coordinates,
            size = size,
            type = type,
            nextCoordinates = nextCoordinates,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId),
            priority = priority,
            streamDistance = streamDistance
    )

    @JvmOverloads
    fun createStreamableRaceCheckpoint(
            sphere: Sphere,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D? = null,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            priority: Int = 0,
            streamDistance: Float = 20f
    ): StreamableRaceCheckpoint = raceCheckpointStreamer.createRaceCheckpoint(
            coordinates = vector3DOf(x = sphere.x, y = sphere.y, z = sphere.z),
            size = sphere.radius,
            type = type,
            nextCoordinates = nextCoordinates,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            priority = priority,
            streamDistance = streamDistance
    )

    @JvmOverloads
    fun createStreamableRaceCheckpoint(
            sphere: Sphere,
            type: RaceCheckpointType,
            interiorId: Int,
            virtualWorldId: Int,
            nextCoordinates: Vector3D? = null,
            priority: Int = 0,
            streamDistance: Float = 20f
    ): StreamableRaceCheckpoint = raceCheckpointStreamer.createRaceCheckpoint(
            coordinates = vector3DOf(x = sphere.x, y = sphere.y, z = sphere.z),
            size = sphere.radius,
            type = type,
            nextCoordinates = nextCoordinates,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId),
            priority = priority,
            streamDistance = streamDistance
    )

}