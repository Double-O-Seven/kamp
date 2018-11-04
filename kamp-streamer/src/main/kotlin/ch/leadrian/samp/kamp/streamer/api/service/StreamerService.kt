package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
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
            interiorId: Int,
            virtualWorldId: Int
    ): StreamableMapObject = mapObjectStreamer.createMapObject(
            modelId = modelId,
            priority = priority,
            streamDistance = streamDistance,
            coordinates = coordinates,
            rotation = rotation,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId)
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

    fun setMaxStreamedInMapObjects(maxStreamedInMapObjects: Int) {
        mapObjectStreamer.capacity = maxStreamedInMapObjects
    }

}