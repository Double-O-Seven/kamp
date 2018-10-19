package ch.leadrian.samp.kamp.streamer.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject
import ch.leadrian.samp.kamp.streamer.util.TimeProvider
import javax.inject.Inject

internal class StreamableMapObjectFactory
@Inject
constructor(
        private val playerMapObjectService: PlayerMapObjectService,
        private val timeProvider: TimeProvider,
        private val timerExecutor: TimerExecutor,
        private val onStreamableMapObjectMovedHandler: OnStreamableMapObjectMovedHandler,
        private val textProvider: TextProvider
) {

    fun create(
            modelId: Int,
            priority: Int,
            streamDistance: Float,
            coordinates: Vector3D,
            rotation: Vector3D,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf()
    ): StreamableMapObject {
        return StreamableMapObject(
                modelId = modelId,
                priority = priority,
                streamDistance = streamDistance,
                coordinates = coordinates,
                rotation = rotation,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                playerMapObjectService = playerMapObjectService,
                timeProvider = timeProvider,
                timerExecutor = timerExecutor,
                onStreamableMapObjectMovedHandler = onStreamableMapObjectMovedHandler,
                textProvider = textProvider
        )
    }

}