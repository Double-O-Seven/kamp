package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect

abstract class SpatiallyIndexedStreamable<S : SpatiallyIndexedStreamable<S, T>, T : HyperRect<*>> : Streamable {

    private val onBoundingBoxChangedHandlers: MutableList<S.(T) -> Unit> = mutableListOf()

    abstract fun getBoundingBox(): T

    fun onBoundingBoxChanged(onBoundingBoxChanged: S.(T) -> Unit) {
        onBoundingBoxChangedHandlers += onBoundingBoxChanged
    }

    protected fun onBoundingBoxChanged() {
        val boundingBox = getBoundingBox()
        onBoundingBoxChangedHandlers.forEach {
            @Suppress("UNCHECKED_CAST")
            it.invoke(this as S, boundingBox)
        }
    }

    internal lateinit var spatialIndexEntry: SpatialIndex.Entry<S, T>


}