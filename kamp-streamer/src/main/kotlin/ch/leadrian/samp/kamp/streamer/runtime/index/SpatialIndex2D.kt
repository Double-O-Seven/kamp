package ch.leadrian.samp.kamp.streamer.runtime.index

import ch.leadrian.samp.kamp.streamer.entity.SpatiallyIndexedStreamable
import com.conversantmedia.util.collection.geometry.Rect2d
import com.conversantmedia.util.collection.spatial.HyperPoint

class SpatialIndex2D<S : SpatiallyIndexedStreamable<S, Rect2d>> : SpatialIndex<S, Rect2d>() {

    override fun getMbr(p1: HyperPoint, p2: HyperPoint): Rect2d =
            Rect2d(
                    p1.getCoord(0),
                    p1.getCoord(1),
                    p2.getCoord(0),
                    p2.getCoord(1)
            )
}