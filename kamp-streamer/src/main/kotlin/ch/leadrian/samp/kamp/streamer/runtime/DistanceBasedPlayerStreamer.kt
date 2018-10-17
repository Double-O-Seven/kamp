package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.streamer.entity.DistanceBasedPlayerStreamable
import ch.leadrian.samp.kamp.streamer.entity.StreamLocation
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import java.util.stream.Collectors.toSet
import java.util.stream.Stream

abstract class DistanceBasedPlayerStreamer<T : DistanceBasedPlayerStreamable>(
        private val capacity: Int,
        private val asyncExecutor: AsyncExecutor,
        playerService: PlayerService
) : Streamer, OnPlayerDisconnectListener {

    private val streamedInStreamables: Multimap<Player, T> = ArrayListMultimap.create(playerService.getMaxPlayers(), capacity)

    private val byPriorityDescendingAndDistanceAscending: Comparator<StreamingInfo<T>> = Comparator
            .comparing<StreamingInfo<T>, Int> { it.streamable.priority }
            .reversed()
            .thenComparing(Comparator.comparing<StreamingInfo<T>, Float> { it.distance })

    override fun stream(streamLocations: List<StreamLocation>) {
        streamLocations.forEach { streamLocation ->
            val newStreamables: Set<T> = getStreamedInStreamables(streamLocation)
            streamOnMainThread(streamLocation.player, newStreamables)
        }
    }

    private fun getStreamedInStreamables(streamLocation: StreamLocation): Set<T> {
        return getStreamInCandidates(streamLocation)
                .filter { !it.isDestroyed }
                .map { StreamingInfo(it, it.distanceTo(streamLocation.location)) }
                .filter { it.distance <= it.streamable.streamDistance }
                .sorted(byPriorityDescendingAndDistanceAscending)
                .limit(capacity.toLong())
                .map { it.streamable }
                .collect(toSet())
    }

    private fun streamOnMainThread(player: Player, newStreamables: Set<T>) {
        asyncExecutor.executeOnMainThread {
            if (player.isConnected) {
                val currentStreamables = streamedInStreamables[player]!!
                streamOut(player, currentStreamables, newStreamables)
                streamIn(player, newStreamables)
            }
        }
    }

    private fun streamOut(forPlayer: Player, currentStreamables: MutableCollection<T>, newStreamables: Set<T>) {
        val iterator = currentStreamables.iterator()
        while (iterator.hasNext()) {
            val streamable = iterator.next()
            val isDestroyed = streamable.isDestroyed
            if (isDestroyed || !newStreamables.contains(streamable)) {
                iterator.remove()
                if (!isDestroyed) {
                    streamedInStreamables.remove(forPlayer, streamable)
                    streamable.onStreamOut(forPlayer)
                }
            }
        }
    }

    private fun streamIn(forPlayer: Player, newStreamables: Set<T>) {
        newStreamables.forEach {
            if (it.isDestroyed || it.isStreamedIn(forPlayer)) return@forEach
            it.onStreamIn(forPlayer)
            streamedInStreamables.put(forPlayer, it)
        }
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        streamedInStreamables.removeAll(player)
    }

    protected abstract fun getStreamInCandidates(streamLocation: StreamLocation): Stream<T>

    private class StreamingInfo<T : DistanceBasedPlayerStreamable>(val streamable: T, val distance: Float)

}