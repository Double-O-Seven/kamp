package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.OnDestroyListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.runtime.entity.CoordinatesBasedPlayerStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.reflect.full.safeCast

class CoordinatesBasedPlayerStreamer<S : CoordinatesBasedPlayerStreamable<S, T>, T : HyperRect<*>>
internal constructor(
        private val spatialIndex: SpatialIndex<S, T>,
        private val streamableClass: KClass<S>,
        maxCapacity: Int,
        distanceBasedPlayerStreamerFactory: DistanceBasedPlayerStreamerFactory
) : Streamer, StreamInCandidateSupplier<S>, OnDestroyListener, OnPlayerDisconnectListener {

    /*
     * The spatial index and set of moving or attached objects may only be accessed during
     * streaming to avoid any race conditions and expensive synchronization.
     * It is less expensive to simple queue some indexing tasks and then execute them on the streaming thread.
     */
    private val nonIndexedStreamables = HashSet<S>()

    private val delegate: DistanceBasedPlayerStreamer<S> = distanceBasedPlayerStreamerFactory.create(
            maxCapacity = maxCapacity,
            streamInCandidateSupplier = this
    )

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @JvmOverloads
    fun add(streamable: S, addToSpatialIndex: Boolean = true) {
        streamable.addOnDestroyListener(this)
        delegate.beforeStream {
            if (addToSpatialIndex) {
                spatialIndex.add(streamable)
            } else {
                nonIndexedStreamables.add(streamable)
            }
        }
    }

    fun remove(streamable: S) {
        streamable.removeOnDestroyListener(this)
        delegate.beforeStream {
            val removed = nonIndexedStreamables.remove(streamable)
            if (!removed) {
                spatialIndex.remove(streamable)
            }
        }
    }

    fun removeFromSpatialIndex(streamable: S) {
        delegate.beforeStream {
            val added = nonIndexedStreamables.add(streamable)
            if (added) {
                spatialIndex.remove(streamable)
            }
        }
    }

    fun onBoundingBoxChange(streamable: S) {
        delegate.beforeStream {
            val isIndexed = !nonIndexedStreamables.contains(streamable)
            if (isIndexed) {
                spatialIndex.update(streamable)
            }
        }
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }

    override fun getStreamInCandidates(streamLocation: StreamLocation): Stream<S> =
            Stream.concat(
                    spatialIndex.getIntersections(streamLocation.location).stream(),
                    nonIndexedStreamables.stream()
            )

    override fun onDestroy(destroyable: Destroyable) {
        streamableClass.safeCast(destroyable)?.let { remove(it) }
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        delegate.onPlayerDisconnect(player, reason)
    }
}