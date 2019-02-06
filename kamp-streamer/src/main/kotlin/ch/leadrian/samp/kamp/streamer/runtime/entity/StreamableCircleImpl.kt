package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Circle
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableCircle
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect2d
import com.conversantmedia.util.collection.geometry.Rect2d

internal class StreamableCircleImpl(
        circle: Circle,
        interiorIds: MutableSet<Int>,
        virtualWorldIds: MutableSet<Int>,
        priority: Int,
        onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler,
        onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler
) :
        AbstractStreamableArea2D(
                interiorIds,
                virtualWorldIds,
                priority,
                onPlayerEnterStreamableAreaHandler,
                onPlayerLeaveStreamableAreaHandler
        ),
        StreamableCircle,
        Circle by circle.toCircle() {

    override val boundingBox: Rect2d = circle.toRect2d()

}