package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.OnDestroyListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.ifNotDestroyed
import ch.leadrian.samp.kamp.streamer.runtime.entity.AbstractStreamableArea
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import ch.leadrian.samp.kamp.streamer.runtime.index.SpatialIndex
import com.conversantmedia.util.collection.spatial.HyperRect
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.full.safeCast

internal abstract class AreaStreamer<T : HyperRect<*>>(
        private val asyncExecutor: AsyncExecutor,
        private val spatialIndex: SpatialIndex<AbstractStreamableArea<T>, T>
) : Streamer, OnDestroyListener, OnPlayerDisconnectListener {

    private val currentPlayerAreas: Multimap<Player, AbstractStreamableArea<T>> = HashMultimap.create()

    private val beforeStreamActions = ConcurrentLinkedQueue<() -> Unit>()

    fun add(streamableArea: AbstractStreamableArea<T>) {
        streamableArea.addOnDestroyListener(this)
        beforeStream {
            spatialIndex.add(streamableArea)
        }
    }

    final override fun stream(streamLocations: List<StreamLocation>) {
        beforeStream()
        streamLocations.forEach { stream(it) }
    }

    private fun stream(streamLocation: StreamLocation) {
        val intersectingAreas = getIntersectingAreas(streamLocation)
        val player = streamLocation.player
        val currentAreas = currentPlayerAreas[player]
        val enteredAreas = (intersectingAreas - currentAreas).sortedByDescending { it.priority }
        val leftAreas = (currentAreas - intersectingAreas).sortedByDescending { it.priority }
        currentAreas -= leftAreas
        currentAreas += enteredAreas
        streamOnMainThread(player, leftAreas, enteredAreas)
    }

    private fun streamOnMainThread(
            player: Player,
            leftAreas: List<AbstractStreamableArea<T>>,
            enteredAreas: List<AbstractStreamableArea<T>>
    ) {
        asyncExecutor.executeOnMainThread {
            if (!player.isConnected) {
                return@executeOnMainThread
            }
            leftAreas.forEach {
                it.ifNotDestroyed { onLeave(player) }
            }
            enteredAreas.forEach {
                it.ifNotDestroyed { onEnter(player) }
            }
        }
    }

    private fun getIntersectingAreas(streamLocation: StreamLocation): List<AbstractStreamableArea<T>> {
        return spatialIndex
                .getIntersections(streamLocation.location)
                .filter { streamLocation.location in it }
                .filter { it.isActive(streamLocation.player) }
    }

    private fun beforeStream(action: () -> Unit) {
        beforeStreamActions += action
    }

    private fun beforeStream() {
        do {
            val action = beforeStreamActions.poll() ?: break
            action()
        } while (true)
    }

    final override fun onDestroy(destroyable: Destroyable) {
        AbstractStreamableArea::class.safeCast(destroyable)?.let {
            @Suppress("UNCHECKED_CAST")
            val streamableArea = it as AbstractStreamableArea<T>
            beforeStream {
                spatialIndex.remove(streamableArea)
                currentPlayerAreas.values().remove(streamableArea)
            }
        }
    }

    final override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        beforeStream {
            currentPlayerAreas.removeAll(player)
        }
    }
}