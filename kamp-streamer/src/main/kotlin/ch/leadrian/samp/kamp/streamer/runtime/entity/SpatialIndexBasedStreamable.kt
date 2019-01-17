package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect

abstract class SpatialIndexBasedStreamable<S : SpatialIndexBasedStreamable<S, T>, T : HyperRect<*>> :
        AbstractStreamable() {

    abstract fun getBoundingBox(): T

    internal lateinit var spatialIndexEntry: SpatialIndex.Entry<S, T>

}