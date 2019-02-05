package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.service.RaceCheckpointService
import ch.leadrian.samp.kamp.streamer.runtime.RaceCheckpointStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableRaceCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableRaceCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableRaceCheckpointImpl
import javax.inject.Inject

internal class StreamableRaceCheckpointFactory
@Inject
constructor(
        private val raceCheckpointService: RaceCheckpointService,
        private val onPlayerEnterStreamableRaceCheckpointHandler: OnPlayerEnterStreamableRaceCheckpointHandler,
        private val onPlayerLeaveStreamableRaceCheckpointHandler: OnPlayerLeaveStreamableRaceCheckpointHandler,
        private val onStreamableRaceCheckpointStreamInHandler: OnStreamableRaceCheckpointStreamInHandler,
        private val onStreamableRaceCheckpointStreamOutHandler: OnStreamableRaceCheckpointStreamOutHandler
) {

    fun create(
            coordinates: Vector3D,
            size: Float,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D?,
            priority: Int,
            streamDistance: Float,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>,
            raceCheckpointStreamer: RaceCheckpointStreamer
    ): StreamableRaceCheckpointImpl {
        return StreamableRaceCheckpointImpl(
                coordinates = coordinates,
                size = size,
                type = type,
                nextCoordinates = nextCoordinates,
                priority = priority,
                streamDistance = streamDistance,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                raceCheckpointStreamer = raceCheckpointStreamer,
                raceCheckpointService = raceCheckpointService,
                onStreamableRaceCheckpointStreamInHandler = onStreamableRaceCheckpointStreamInHandler,
                onStreamableRaceCheckpointStreamOutHandler = onStreamableRaceCheckpointStreamOutHandler,
                onPlayerEnterStreamableRaceCheckpointHandler = onPlayerEnterStreamableRaceCheckpointHandler,
                onPlayerLeaveStreamableRaceCheckpointHandler = onPlayerLeaveStreamableRaceCheckpointHandler
        )
    }

}