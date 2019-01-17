package ch.leadrian.samp.kamp.streamer.runtime.index

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.streamer.runtime.entity.SpatialIndexBasedStreamable
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex.Entry
import com.conversantmedia.util.collection.geometry.Rect3d
import com.conversantmedia.util.collection.spatial.HyperPoint
import com.conversantmedia.util.collection.spatial.HyperRect
import com.conversantmedia.util.collection.spatial.RectBuilder
import com.conversantmedia.util.collection.spatial.SpatialSearch
import com.conversantmedia.util.collection.spatial.SpatialSearches
import java.util.LinkedList

abstract class SpatialIndex<S : SpatialIndexBasedStreamable<S, T>, T : HyperRect<*>> : RectBuilder<Entry<S, T>> {

    private val rTree: SpatialSearch<Entry<S, T>> by lazy { SpatialSearches.rTree(this) }

    override fun getBBox(t: Entry<S, T>): HyperRect<*> = t.boundingBox

    abstract override fun getMbr(p1: HyperPoint, p2: HyperPoint): HyperRect<*>

    fun add(streamable: S) {
        rTree.add(newEntry(streamable))
    }

    fun remove(streamable: S) {
        rTree.remove(streamable.spatialIndexEntry)
    }

    fun update(streamable: S) {
        val oldEntry = streamable.spatialIndexEntry
        rTree.update(oldEntry, newEntry(streamable))
    }

    fun getIntersections(coordinates: Vector3D): List<S> {
        val streamInCandidates = LinkedList<S>()
        with(coordinates) {
            val x = x.toDouble()
            val y = y.toDouble()
            val z = z.toDouble()
            rTree.intersects(Rect3d(x, y, z, x, y, z)) { streamInCandidates.add(it.streamable) }
        }
        return streamInCandidates
    }

    private fun newEntry(streamable: S): Entry<S, T> {
        return Entry(streamable).also {
            streamable.spatialIndexEntry = it
        }
    }

    class Entry<S : SpatialIndexBasedStreamable<S, T>, T : HyperRect<*>>
    internal constructor(internal val streamable: S) {

        val boundingBox: T = streamable.getBoundingBox()

    }
}