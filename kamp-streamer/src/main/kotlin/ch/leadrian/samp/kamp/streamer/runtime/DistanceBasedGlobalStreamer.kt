package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.entity.ifNotDestroyed
import ch.leadrian.samp.kamp.streamer.runtime.entity.DistanceBasedGlobalStreamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Collectors.toList
import java.util.stream.Collectors.toSet
import java.util.stream.Stream
import kotlin.properties.Delegates

class DistanceBasedGlobalStreamer<T : DistanceBasedGlobalStreamable>(
        maxCapacity: Int,
        private val asyncExecutor: AsyncExecutor,
        private val streamInCandidateSupplier: StreamInCandidateSupplier<T>
) : Streamer {

    private val streamedInStreamables: MutableSet<T> = mutableSetOf()

    private val beforeStreamActions = ConcurrentLinkedQueue<() -> Unit>()

    private val byPriorityDescendingAndDistanceAscending: Comparator<StreamingInfo<T>> = Comparator
            .comparing<StreamingInfo<T>, Int> { it.streamable.priority }
            .reversed()
            .thenComparing(Comparator.comparing<StreamingInfo<T>, Float> { it.distance })

    var capacity: Int by Delegates.vetoable(maxCapacity) { _, _, newCapacity -> newCapacity in 0..maxCapacity }

    override fun stream(streamLocations: List<StreamLocation>) {
        beforeStream()
        val newStreamables: Set<T> = getStreamedInStreamables(streamLocations)
        streamOnMainThread(newStreamables)
    }

    fun beforeStream(action: () -> Unit) {
        beforeStreamActions += action
    }

    fun isStreamedIn(streamable: T): Boolean = streamedInStreamables.contains(streamable)

    private fun beforeStream() {
        do {
            val action = beforeStreamActions.poll() ?: break
            action()
        } while (true)
    }

    private fun getStreamedInStreamables(streamLocations: List<StreamLocation>): Set<T> {
        return streamLocations
                .stream()
                .flatMap { streamLocation ->
                    streamInCandidateSupplier
                            .getStreamInCandidates(streamLocation)
                            .collect(toList())
                            .takeNotDestroyedStreamablesInRange(streamLocation)
                }
                .distinct()
                .takeClosestStreamables()
                .map { it.streamable }
                .collect(toSet())
    }

    private fun List<T>.takeNotDestroyedStreamablesInRange(streamLocation: StreamLocation): Stream<StreamingInfo<T>> {
        return asyncExecutor
                .computeOnMainThread {
                    asSequence()
                            .filter { !it.isDestroyed }
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

    private fun streamOnMainThread(newStreamables: Set<T>) {
        asyncExecutor.executeOnMainThread {
            streamOut(newStreamables)
            streamIn(newStreamables)
        }
    }

    private fun streamIn(newStreamables: Set<T>) {
        newStreamables.forEach {
            it.ifNotDestroyed {
                onStreamIn()
                streamedInStreamables.add(this)
            }
        }
    }

    private fun streamOut(newStreamables: Set<T>) {
        val iterator = streamedInStreamables.iterator()
        while (iterator.hasNext()) {
            val streamable = iterator.next()
            val isDestroyed = streamable.isDestroyed
            if (isDestroyed || streamable !in newStreamables) {
                iterator.remove()
                if (!isDestroyed) {
                    streamable.onStreamOut()
                }
            }
        }
    }

    private class StreamingInfo<T : DistanceBasedGlobalStreamable>(val streamable: T, val distance: Float)

}