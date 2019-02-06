package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableArea2D
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import com.conversantmedia.util.collection.geometry.Rect2d

internal abstract class AbstractStreamableArea2D(
        interiorIds: MutableSet<Int>,
        virtualWorldIds: MutableSet<Int>,
        priority: Int,
        onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler,
        onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler
) : AbstractStreamableArea<Rect2d>(
        interiorIds,
        virtualWorldIds,
        priority,
        onPlayerEnterStreamableAreaHandler,
        onPlayerLeaveStreamableAreaHandler
), StreamableArea2D {

    override fun contains(player: Player): Boolean = contains(player.location)

    override fun contains(coordinates: Vector3D): Boolean = contains(coordinates as Vector2D)

}