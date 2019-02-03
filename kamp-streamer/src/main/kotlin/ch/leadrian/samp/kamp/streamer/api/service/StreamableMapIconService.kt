package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapIcon
import ch.leadrian.samp.kamp.streamer.runtime.MapIconStreamer
import javax.inject.Inject

class StreamableMapIconService
@Inject
internal constructor(private val mapIconStreamer: MapIconStreamer) {

    @JvmOverloads
    fun createStreamableMapIcon(
            coordinates: Vector3D,
            type: MapIconType,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            style: MapIconStyle = MapIconStyle.LOCAL,
            color: Color = Colors.WHITE,
            priority: Int = 0,
            streamDistance: Float = 300f
    ): StreamableMapIcon = mapIconStreamer.createMapIcon(
            coordinates = coordinates,
            type = type,
            color = color,
            style = style,
            interiorIds = interiorIds,
            virtualWorldIds = virtualWorldIds,
            priority = priority,
            streamDistance = streamDistance
    )

    @JvmOverloads
    fun createStreamableMapIcon(
            coordinates: Vector3D,
            type: MapIconType,
            interiorId: Int,
            virtualWorldId: Int,
            style: MapIconStyle = MapIconStyle.LOCAL,
            color: Color = Colors.WHITE,
            priority: Int = 0,
            streamDistance: Float = 300f
    ): StreamableMapIcon = mapIconStreamer.createMapIcon(
            coordinates = coordinates,
            type = type,
            color = color,
            style = style,
            interiorIds = mutableSetOf(interiorId),
            virtualWorldIds = mutableSetOf(virtualWorldId),
            priority = priority,
            streamDistance = streamDistance
    )

    @JvmOverloads
    fun createStreamableMapIcon(
            location: Location,
            type: MapIconType,
            style: MapIconStyle = MapIconStyle.LOCAL,
            color: Color = Colors.WHITE,
            priority: Int = 0,
            streamDistance: Float = 300f
    ): StreamableMapIcon = mapIconStreamer.createMapIcon(
            coordinates = location,
            type = type,
            color = color,
            style = style,
            interiorIds = mutableSetOf(location.interiorId),
            virtualWorldIds = mutableSetOf(location.virtualWorldId),
            priority = priority,
            streamDistance = streamDistance
    )

    fun setMaxStreamedInMapIcons(maxStreamedInMapIcons: Int) {
        mapIconStreamer.capacity = maxStreamedInMapIcons
    }

}