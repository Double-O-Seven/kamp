package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableRectangle
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect2d
import com.conversantmedia.util.collection.geometry.Rect2d

internal class StreamableRectangleImpl(
        rectangle: Rectangle,
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
        StreamableRectangle,
        Rectangle by rectangle.toRectangle() {

    override val boundingBox: Rect2d = rectangle.toRect2d()

}