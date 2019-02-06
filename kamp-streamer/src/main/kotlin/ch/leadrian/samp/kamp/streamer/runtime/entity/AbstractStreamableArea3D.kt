package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableArea3D
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import com.conversantmedia.util.collection.geometry.Rect3d

internal abstract class AbstractStreamableArea3D(
        interiorIds: MutableSet<Int>,
        virtualWorldIds: MutableSet<Int>,
        priority: Int,
        onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler,
        onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler
) : AbstractStreamableArea<Rect3d>(
        interiorIds,
        virtualWorldIds,
        priority,
        onPlayerEnterStreamableAreaHandler,
        onPlayerLeaveStreamableAreaHandler
), StreamableArea3D {

    override fun contains(player: Player): Boolean = contains(player.location)

}