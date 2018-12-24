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
    private val movingStreamables = HashSet<S>()

    private val delegate: DistanceBasedPlayerStreamer<S> = distanceBasedPlayerStreamerFactory.create(
            maxCapacity = maxCapacity,
            streamInCandidateSupplier = this
    )

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    fun onBoundingBoxChange(streamable: S) {
        delegate.beforeStream {
            if (!movingStreamables.contains(streamable)) {
                spatialIndex.update(streamable)
            }
        }
    }

    fun onStateChange(streamable: S) {
        if (streamable.isMoving) {
            delegate.beforeStream {
                if (movingStreamables.add(streamable)) {
                    spatialIndex.remove(streamable)
                }
            }
        }
    }

    fun add(streamable: S) {
        streamable.addOnDestroyListener(this)
        delegate.beforeStream {
            spatialIndex.add(streamable)
        }
    }

    fun remove(streamable: S) {
        streamable.removeOnDestroyListener(this)
        delegate.beforeStream {
            movingStreamables -= streamable
            spatialIndex.remove(streamable)
        }
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        delegate.stream(streamLocations)
    }

    override fun getStreamInCandidates(streamLocation: StreamLocation): Stream<S> =
            Stream.concat(
                    spatialIndex.getIntersections(streamLocation.location).stream(),
                    movingStreamables.stream()
            )

    override fun onDestroy(destroyable: Destroyable) {
        streamableClass.safeCast(destroyable)?.let { remove(it) }
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        delegate.onPlayerDisconnect(player, reason)
    }
}