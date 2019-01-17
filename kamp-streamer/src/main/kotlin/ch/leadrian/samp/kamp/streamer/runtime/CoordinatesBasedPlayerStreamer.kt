package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.runtime.entity.CoordinatesBasedPlayerStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect
import kotlin.reflect.KClass

class CoordinatesBasedPlayerStreamer<S : CoordinatesBasedPlayerStreamable<S, T>, T : HyperRect<*>>
internal constructor(
        spatialIndex: SpatialIndex<S, T>,
        streamableClass: KClass<S>,
        maxCapacity: Int,
        distanceBasedPlayerStreamerFactory: DistanceBasedPlayerStreamerFactory,
        spatialIndexBasedStreamableContainerFactory: SpatialIndexBasedStreamableContainerFactory
) : Streamer, OnPlayerDisconnectListener {

    private val streamableContainer: SpatialIndexBasedStreamableContainer<S, T> =
            spatialIndexBasedStreamableContainerFactory.create(spatialIndex, streamableClass)

    private val delegate: DistanceBasedPlayerStreamer<S> =
            distanceBasedPlayerStreamerFactory.create(maxCapacity, streamableContainer)

    var capacity: Int
        get() = delegate.capacity
        set(value) {
            delegate.capacity = value
        }

    @JvmOverloads
    fun add(streamable: S, addToSpatialIndex: Boolean = true) {
        streamableContainer.add(streamable, addToSpatialIndex)
    }

    fun remove(streamable: S) {
        streamableContainer.remove(streamable)
    }

    fun removeFromSpatialIndex(streamable: S) {
        streamableContainer.removeFromSpatialIndex(streamable)
    }

    fun onBoundingBoxChange(streamable: S) {
        streamableContainer.onBoundingBoxChange(streamable)
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        streamableContainer.onStream()
        delegate.stream(streamLocations)
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        delegate.onPlayerDisconnect(player, reason)
    }
}