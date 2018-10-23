package ch.leadrian.samp.kamp.streamer.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObjectStateFactory
import javax.inject.Inject

internal class StreamableMapObjectFactory
@Inject
constructor(
        private val playerMapObjectService: PlayerMapObjectService,
        private val onStreamableMapObjectMovedHandler: OnStreamableMapObjectMovedHandler,
        private val onPlayerEditStreamableMapObjectHandler: OnPlayerEditStreamableMapObjectHandler,
        private val onPlayerSelectStreamableMapObjectHandler: OnPlayerSelectStreamableMapObjectHandler,
        private val textProvider: TextProvider,
        private val streamableMapObjectStateFactory: StreamableMapObjectStateFactory
) {

    fun create(
            modelId: Int,
            priority: Int,
            streamDistance: Float,
            coordinates: Vector3D,
            rotation: Vector3D,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>
    ): StreamableMapObject {
        return StreamableMapObject(
                modelId = modelId,
                priority = priority,
                streamDistance = streamDistance,
                coordinates = coordinates.toVector3D(),
                rotation = rotation.toVector3D(),
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                playerMapObjectService = playerMapObjectService,
                onStreamableMapObjectMovedHandler = onStreamableMapObjectMovedHandler,
                onPlayerEditStreamableMapObjectHandler = onPlayerEditStreamableMapObjectHandler,
                onPlayerSelectStreamableMapObjectHandler = onPlayerSelectStreamableMapObjectHandler,
                textProvider = textProvider,
                streamableMapObjectStateFactory = streamableMapObjectStateFactory
        )
    }

}