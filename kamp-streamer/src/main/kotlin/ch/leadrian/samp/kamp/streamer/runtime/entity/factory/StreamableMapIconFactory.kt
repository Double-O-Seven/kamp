package ch.leadrian.samp.kamp.streamer.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.MapIconStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.PlayerMapIconIdAllocator
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapIconImpl
import javax.inject.Inject

internal class StreamableMapIconFactory
@Inject
constructor(
        private val playerMapIconIdAllocator: PlayerMapIconIdAllocator,
        private val onStreamableMapIconStreamInHandler: OnStreamableMapIconStreamInHandler,
        private val onStreamableMapIconStreamOutHandler: OnStreamableMapIconStreamOutHandler
) {

    fun create(
            coordinates: Vector3D,
            type: MapIconType,
            color: Color,
            style: MapIconStyle,
            priority: Int,
            streamDistance: Float,
            interiorIds: MutableSet<Int>,
            virtualWorldIds: MutableSet<Int>,
            mapIconStreamer: MapIconStreamer
    ): StreamableMapIconImpl {
        return StreamableMapIconImpl(
                coordinates = coordinates,
                type = type,
                color = color,
                style = style,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                priority = priority,
                streamDistance = streamDistance,
                mapIconStreamer = mapIconStreamer,
                onStreamableMapIconStreamInHandler = onStreamableMapIconStreamInHandler,
                onStreamableMapIconStreamOutHandler = onStreamableMapIconStreamOutHandler,
                playerMapIconIdAllocator = playerMapIconIdAllocator
        )
    }

}