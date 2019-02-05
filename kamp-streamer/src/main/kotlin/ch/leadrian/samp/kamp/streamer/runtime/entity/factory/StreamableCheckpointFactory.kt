package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.service.CheckpointService
import ch.leadrian.samp.kamp.streamer.runtime.CheckpointStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableCheckpointImpl
import javax.inject.Inject

internal class StreamableCheckpointFactory
@Inject
constructor(
        private val checkpointService: CheckpointService,
        private val onPlayerEnterStreamableCheckpointHandler: OnPlayerEnterStreamableCheckpointHandler,
        private val onPlayerLeaveStreamableCheckpointHandler: OnPlayerLeaveStreamableCheckpointHandler,
        private val onStreamableCheckpointStreamInHandler: OnStreamableCheckpointStreamInHandler,
        private val onStreamableCheckpointStreamOutHandler: OnStreamableCheckpointStreamOutHandler
) {

    fun create(
            coordinates: Vector3D,
            size: Float,
            priority: Int,
            streamDistance: Float,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>,
            checkpointStreamer: CheckpointStreamer
    ): StreamableCheckpointImpl {
        return StreamableCheckpointImpl(
                coordinates = coordinates,
                size = size,
                priority = priority,
                streamDistance = streamDistance,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                checkpointStreamer = checkpointStreamer,
                checkpointService = checkpointService,
                onStreamableCheckpointStreamInHandler = onStreamableCheckpointStreamInHandler,
                onStreamableCheckpointStreamOutHandler = onStreamableCheckpointStreamOutHandler,
                onPlayerEnterStreamableCheckpointHandler = onPlayerEnterStreamableCheckpointHandler,
                onPlayerLeaveStreamableCheckpointHandler = onPlayerLeaveStreamableCheckpointHandler
        )
    }

}