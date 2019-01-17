package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.OnDestroyListener
import ch.leadrian.samp.kamp.streamer.runtime.entity.SpatialIndexBasedStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.reflect.full.safeCast

class SpatialIndexBasedStreamableContainer<S : SpatialIndexBasedStreamable<S, T>, T : HyperRect<*>>
internal constructor(
        private val spatialIndex: SpatialIndex<S, T>,
        private val streamableClass: KClass<S>
) : StreamInCandidateSupplier<S>, OnDestroyListener {

    /*
     * The spatial index and set of moving or attached objects may only be accessed during
     * streaming to avoid any race conditions and expensive synchronization.
     * It is less expensive to simple queue some indexing tasks and then execute them on the streaming thread.
     */
    private val nonIndexedStreamables = HashSet<S>()

    private val onStreamActions = ConcurrentLinkedQueue<() -> Unit>()

    @JvmOverloads
    fun add(streamable: S, addToSpatialIndex: Boolean = true) {
        streamable.addOnDestroyListener(this)
        onStream {
            if (addToSpatialIndex) {
                spatialIndex.add(streamable)
            } else {
                nonIndexedStreamables.add(streamable)
            }
        }
    }

    fun remove(streamable: S) {
        streamable.removeOnDestroyListener(this)
        onStream {
            val removed = nonIndexedStreamables.remove(streamable)
            if (!removed) {
                spatialIndex.remove(streamable)
            }
        }
    }

    fun removeFromSpatialIndex(streamable: S) {
        onStream {
            val added = nonIndexedStreamables.add(streamable)
            if (added) {
                spatialIndex.remove(streamable)
            }
        }
    }

    fun onBoundingBoxChange(streamable: S) {
        onStream {
            val isIndexed = !nonIndexedStreamables.contains(streamable)
            if (isIndexed) {
                spatialIndex.update(streamable)
            }
        }
    }

    private fun onStream(action: () -> Unit) {
        onStreamActions += action
    }

    fun onStream() {
        do {
            val action = onStreamActions.poll() ?: break
            action()
        } while (true)
    }

    override fun getStreamInCandidates(streamLocation: StreamLocation): Stream<S> =
            Stream.concat(
                    spatialIndex.getIntersections(streamLocation.location).stream(),
                    nonIndexedStreamables.stream()
            )

    override fun onDestroy(destroyable: Destroyable) {
        streamableClass.safeCast(destroyable)?.let { remove(it) }
    }

}