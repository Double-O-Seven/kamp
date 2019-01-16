package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.ifNotDestroyed
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.streamer.runtime.entity.DistanceBasedPlayerStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Collectors.toList
import java.util.stream.Collectors.toSet
import java.util.stream.Stream
import kotlin.properties.Delegates

class DistanceBasedPlayerStreamer<T : DistanceBasedPlayerStreamable>(
        maxCapacity: Int,
        private val asyncExecutor: AsyncExecutor,
        private val streamInCandidateSupplier: StreamInCandidateSupplier<T>,
        playerService: PlayerService
) : Streamer, OnPlayerDisconnectListener {

    private val streamedInStreamables: Multimap<Player, T> = ArrayListMultimap.create(
            playerService.getMaxPlayers(),
            maxCapacity
    )

    private val beforeStreamActions = ConcurrentLinkedQueue<() -> Unit>()

    private val byPriorityDescendingAndDistanceAscending: Comparator<StreamingInfo<T>> = Comparator
            .comparing<StreamingInfo<T>, Int> { it.streamable.priority }
            .reversed()
            .thenComparing(Comparator.comparing<StreamingInfo<T>, Float> { it.distance })

    var capacity: Int by Delegates.vetoable(maxCapacity) { _, _, newCapacity -> newCapacity in 0..maxCapacity }

    override fun stream(streamLocations: List<StreamLocation>) {
        beforeStream()
        streamLocations.forEach { streamLocation ->
            val newStreamables: Set<T> = getStreamedInStreamables(streamLocation)
            streamOnMainThread(streamLocation.player, newStreamables)
        }
    }

    fun beforeStream(action: () -> Unit) {
        beforeStreamActions += action
    }

    fun isStreamedIn(streamable: T, forPlayer: Player): Boolean = streamedInStreamables.containsEntry(
            forPlayer,
            streamable
    )

    private fun beforeStream() {
        do {
            val action = beforeStreamActions.poll() ?: break
            action()
        } while (true)
    }

    private fun getStreamedInStreamables(streamLocation: StreamLocation): Set<T> {
        return streamInCandidateSupplier
                .getStreamInCandidates(streamLocation)
                .collect(toList())
                .takeNotDestroyedStreamablesInRange(streamLocation)
                .takeClosestStreamables()
                .map { it.streamable }
                .collect(toSet())
    }

    private fun List<T>.takeNotDestroyedStreamablesInRange(streamLocation: StreamLocation): Stream<StreamingInfo<T>> {
        return asyncExecutor
                .computeOnMainThread {
                    asSequence()
                            .filter { !it.isDestroyed && it.isVisible(streamLocation.player) }
                            .map { StreamingInfo(it, it.distanceTo(streamLocation.location)) }
                            .filter { it.distance <= it.streamable.streamDistance }
                            .toList()
                }
                .get()
                .stream()
    }

    private fun Stream<StreamingInfo<T>>.takeClosestStreamables(): Stream<StreamingInfo<T>> {
        return when {
            capacity <= 0 -> Stream.empty()
            capacity == 1 -> min(byPriorityDescendingAndDistanceAscending).map { Stream.of(it) }.orElse(Stream.empty())
            else -> sorted(byPriorityDescendingAndDistanceAscending).limit(capacity.toLong())
        }
    }

    private fun streamOnMainThread(player: Player, newStreamables: Set<T>) {
        asyncExecutor.executeOnMainThread {
            if (player.isConnected) {
                streamOut(player, newStreamables)
                streamIn(player, newStreamables)
            }
        }
    }

    private fun streamIn(forPlayer: Player, newStreamables: Set<T>) {
        newStreamables.forEach {
            it.ifNotDestroyed {
                if (!isStreamedIn(forPlayer)) {
                    onStreamIn(forPlayer)
                    streamedInStreamables.put(forPlayer, this)
                }
            }
        }
    }

    private fun streamOut(forPlayer: Player, newStreamables: Set<T>) {
        val iterator = streamedInStreamables[forPlayer].iterator()
        while (iterator.hasNext()) {
            val streamable = iterator.next()
            val isDestroyed = streamable.isDestroyed
            if (isDestroyed || !newStreamables.contains(streamable)) {
                iterator.remove()
                if (!isDestroyed) {
                    streamable.onStreamOut(forPlayer)
                }
            }
        }
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        streamedInStreamables.removeAll(player)
    }

    private class StreamingInfo<T : DistanceBasedPlayerStreamable>(val streamable: T, val distance: Float)

}