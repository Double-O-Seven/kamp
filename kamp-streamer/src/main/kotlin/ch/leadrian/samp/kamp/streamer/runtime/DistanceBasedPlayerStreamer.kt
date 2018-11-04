package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.streamer.runtime.entity.DistanceBasedPlayerStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Collectors.toSet
import java.util.stream.Stream
import javax.annotation.PostConstruct
import kotlin.properties.Delegates

abstract class DistanceBasedPlayerStreamer<T : DistanceBasedPlayerStreamable>(
        maxCapacity: Int,
        private val asyncExecutor: AsyncExecutor,
        protected val callbackListenerManager: CallbackListenerManager,
        playerService: PlayerService
) : Streamer, OnPlayerDisconnectListener {

    private val streamedInStreamables: Multimap<Player, T> = ArrayListMultimap.create(playerService.getMaxPlayers(), maxCapacity)

    private val onStreamActions = ConcurrentLinkedQueue<() -> Unit>()

    private val byPriorityDescendingAndDistanceAscending: Comparator<StreamingInfo<T>> = Comparator
            .comparing<StreamingInfo<T>, Int> { it.streamable.priority }
            .reversed()
            .thenComparing(Comparator.comparing<StreamingInfo<T>, Float> { it.distance })

    var capacity: Int by Delegates.vetoable(maxCapacity) { _, _, newCapacity -> newCapacity in 0..maxCapacity }

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun stream(streamLocations: List<StreamLocation>) {
        onStream()
        streamLocations.forEach { streamLocation ->
            val newStreamables: Set<T> = getStreamedInStreamables(streamLocation)
            streamOnMainThread(streamLocation.player, newStreamables)
        }
    }

    private fun getStreamedInStreamables(streamLocation: StreamLocation): Set<T> {
        return getStreamInCandidates(streamLocation)
                .filter { !it.isDestroyed }
                .filter { it.streamInCondition(streamLocation.player) }
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
                val currentStreamables = streamedInStreamables[player]
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

    protected fun onStream(action: () -> Unit) {
        onStreamActions += action
    }

    private fun onStream() {
        do {
            val action = onStreamActions.poll() ?: break
            action()
        } while (true)
    }

    private class StreamingInfo<T : DistanceBasedPlayerStreamable>(val streamable: T, val distance: Float)

}