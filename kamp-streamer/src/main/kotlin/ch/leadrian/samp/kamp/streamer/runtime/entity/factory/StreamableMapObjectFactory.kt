package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectImpl
import javax.inject.Inject

internal class StreamableMapObjectFactory
@Inject
constructor(
        private val playerMapObjectService: PlayerMapObjectService,
        private val onStreamableMapObjectMovedHandler: OnStreamableMapObjectMovedHandler,
        private val onPlayerEditStreamableMapObjectHandler: OnPlayerEditStreamableMapObjectHandler,
        private val onPlayerSelectStreamableMapObjectHandler: OnPlayerSelectStreamableMapObjectHandler,
        private val onStreamableMapObjectStreamInHandler: OnStreamableMapObjectStreamInHandler,
        private val onStreamableMapObjectStreamOutHandler: OnStreamableMapObjectStreamOutHandler,
        private val textProvider: TextProvider,
        private val streamableMapObjectStateMachineFactory: StreamableMapObjectStateMachineFactory
) {

    fun create(
            modelId: Int,
            priority: Int,
            streamDistance: Float,
            coordinates: Vector3D,
            rotation: Vector3D,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>
    ): StreamableMapObjectImpl {
        return StreamableMapObjectImpl(
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
                onStreamableMapObjectStreamInHandler = onStreamableMapObjectStreamInHandler,
                onStreamableMapObjectStreamOutHandler = onStreamableMapObjectStreamOutHandler,
                textProvider = textProvider,
                streamableMapObjectStateMachineFactory = streamableMapObjectStateMachineFactory
        )
    }

}