package ch.leadrian.samp.kamp.streamer.runtime.index

import ch.leadrian.samp.kamp.streamer.runtime.entity.SpatialIndexBasedStreamable
import com.conversantmedia.util.collection.geometry.Rect3d
import com.conversantmedia.util.collection.spatial.HyperPoint

class SpatialIndex3D<S : SpatialIndexBasedStreamable<S, Rect3d>> : SpatialIndex<S, Rect3d>() {

    override fun getMbr(p1: HyperPoint, p2: HyperPoint): Rect3d =
            Rect3d(
                    p1.getCoord(0),
                    p1.getCoord(1),
                    p1.getCoord(2),
                    p2.getCoord(0),
                    p2.getCoord(1),
                    p2.getCoord(2)
            )
}