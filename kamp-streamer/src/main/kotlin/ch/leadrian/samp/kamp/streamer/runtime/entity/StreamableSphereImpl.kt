package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableSphere
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d

internal class StreamableSphereImpl(
        sphere: Sphere,
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
        StreamableSphere,
        Sphere by sphere.toSphere() {

    override val boundingBox: Rect3d = sphere.toRect3d()

}