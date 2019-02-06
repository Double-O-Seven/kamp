package ch.leadrian.samp.kamp.streamer.api.service

import ch.leadrian.samp.kamp.core.api.data.Box
import ch.leadrian.samp.kamp.core.api.data.Circle
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableBox
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableCircle
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableRectangle
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableSphere
import ch.leadrian.samp.kamp.streamer.runtime.Area2DStreamer
import ch.leadrian.samp.kamp.streamer.runtime.Area3DStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableBoxImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableCircleImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableRectangleImpl
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableSphereImpl
import javax.inject.Inject

class StreamableAreaService
@Inject
internal constructor(
        private val area2DStreamer: Area2DStreamer,
        private val area3DStreamer: Area3DStreamer,
        private val onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler,
        private val onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler
) {

    @JvmOverloads
    fun createStreamableRectangle(
            rectangle: Rectangle,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            priority: Int = 0
    ): StreamableRectangle {
        val streamableRectangle = StreamableRectangleImpl(
                rectangle = rectangle,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                priority = priority,
                onPlayerEnterStreamableAreaHandler = onPlayerEnterStreamableAreaHandler,
                onPlayerLeaveStreamableAreaHandler = onPlayerLeaveStreamableAreaHandler
        )
        area2DStreamer.add(streamableRectangle)
        return streamableRectangle
    }

    @JvmOverloads
    fun createStreamableRectangle(
            rectangle: Rectangle,
            interiorId: Int,
            virtualWorldId: Int,
            priority: Int = 0
    ): StreamableRectangle {
        return createStreamableRectangle(
                rectangle = rectangle,
                interiorIds = mutableSetOf(interiorId),
                virtualWorldIds = mutableSetOf(virtualWorldId),
                priority = priority
        )
    }

    @JvmOverloads
    fun createStreamableCircle(
            circle: Circle,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            priority: Int = 0
    ): StreamableCircle {
        val streamableCircle = StreamableCircleImpl(
                circle = circle,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                priority = priority,
                onPlayerEnterStreamableAreaHandler = onPlayerEnterStreamableAreaHandler,
                onPlayerLeaveStreamableAreaHandler = onPlayerLeaveStreamableAreaHandler
        )
        area2DStreamer.add(streamableCircle)
        return streamableCircle
    }

    @JvmOverloads
    fun createStreamableCircle(
            circle: Circle,
            interiorId: Int,
            virtualWorldId: Int,
            priority: Int = 0
    ): StreamableCircle {
        return createStreamableCircle(
                circle = circle,
                interiorIds = mutableSetOf(interiorId),
                virtualWorldIds = mutableSetOf(virtualWorldId),
                priority = priority
        )
    }

    @JvmOverloads
    fun createStreamableBox(
            box: Box,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            priority: Int = 0
    ): StreamableBox {
        val streamableBox = StreamableBoxImpl(
                box = box,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                priority = priority,
                onPlayerEnterStreamableAreaHandler = onPlayerEnterStreamableAreaHandler,
                onPlayerLeaveStreamableAreaHandler = onPlayerLeaveStreamableAreaHandler
        )
        area3DStreamer.add(streamableBox)
        return streamableBox
    }

    @JvmOverloads
    fun createStreamableBox(
            box: Box,
            interiorId: Int,
            virtualWorldId: Int,
            priority: Int = 0
    ): StreamableBox {
        return createStreamableBox(
                box = box,
                interiorIds = mutableSetOf(interiorId),
                virtualWorldIds = mutableSetOf(virtualWorldId),
                priority = priority
        )
    }

    @JvmOverloads
    fun createStreamableSphere(
            sphere: Sphere,
            interiorIds: MutableSet<Int> = mutableSetOf(),
            virtualWorldIds: MutableSet<Int> = mutableSetOf(),
            priority: Int = 0
    ): StreamableSphere {
        val streamableSphere = StreamableSphereImpl(
                sphere = sphere,
                interiorIds = interiorIds,
                virtualWorldIds = virtualWorldIds,
                priority = priority,
                onPlayerEnterStreamableAreaHandler = onPlayerEnterStreamableAreaHandler,
                onPlayerLeaveStreamableAreaHandler = onPlayerLeaveStreamableAreaHandler
        )
        area3DStreamer.add(streamableSphere)
        return streamableSphere
    }

    @JvmOverloads
    fun createStreamableSphere(
            sphere: Sphere,
            interiorId: Int,
            virtualWorldId: Int,
            priority: Int = 0
    ): StreamableSphere {
        return createStreamableSphere(
                sphere = sphere,
                interiorIds = mutableSetOf(interiorId),
                virtualWorldIds = mutableSetOf(virtualWorldId),
                priority = priority
        )
    }

}