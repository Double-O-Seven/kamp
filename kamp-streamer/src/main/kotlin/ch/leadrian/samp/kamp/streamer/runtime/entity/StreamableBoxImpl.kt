package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Box
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableBox
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d

internal class StreamableBoxImpl(
        box: Box,
        interiorIds: MutableSet<Int>,
        virtualWorldIds: MutableSet<Int>,
        priority: Int,
        onPlayerEnterStreamableAreaHandler: OnPlayerEnterStreamableAreaHandler,
        onPlayerLeaveStreamableAreaHandler: OnPlayerLeaveStreamableAreaHandler
) :
        AbstractStreamableArea3D(
                interiorIds,
                virtualWorldIds,
                priority,
                onPlayerEnterStreamableAreaHandler,
                onPlayerLeaveStreamableAreaHandler
        ),
        StreamableBox,
        Box by box.toBox() {

    override val boundingBox: Rect3d = box.toRect3d()

}