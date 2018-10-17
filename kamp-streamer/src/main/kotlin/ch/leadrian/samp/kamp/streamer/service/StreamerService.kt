package ch.leadrian.samp.kamp.streamer.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject
import ch.leadrian.samp.kamp.streamer.runtime.MapObjectStreamer
import javax.inject.Inject

class StreamerService
@Inject
internal constructor(
        private val mapObjectStreamer: MapObjectStreamer
) {

    fun createStreamableMapObject(
            modelId: Int,
            priority: Int,
            streamDistance: Float,
            coordinates: Vector3D,
            rotation: Vector3D,
            interiorId: Int? = null,
            virtualWorldId: Int? = null
    ): StreamableMapObject = mapObjectStreamer.createMapObject(
            modelId = modelId,
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            rotation = rotation,
            interiorIds = interiorId?.let { mutableSetOf(it) } ?: mutableSetOf(),
            virtualWorldIds = virtualWorldId?.let { mutableSetOf(it) } ?: mutableSetOf()
    )

    @JvmOverloads
    fun createStreamableMapObject(
            modelId: Int,
            priority: Int,
            streamDistance: Float,
            coordinates: Vector3D,
            rotation: Vector3D,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf()
    ): StreamableMapObject = mapObjectStreamer.createMapObject(
            modelId = modelId,
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            rotation = rotation,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds
    )

}