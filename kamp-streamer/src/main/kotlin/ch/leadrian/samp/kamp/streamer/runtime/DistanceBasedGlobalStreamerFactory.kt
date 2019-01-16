package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.streamer.runtime.entity.DistanceBasedGlobalStreamable
import javax.inject.Inject

class DistanceBasedGlobalStreamerFactory
@Inject
internal constructor(private val asyncExecutor: AsyncExecutor) {

    fun <T : DistanceBasedGlobalStreamable> create(
            maxCapacity: Int,
            streamInCandidateSupplier: StreamInCandidateSupplier<T>
    ): DistanceBasedGlobalStreamer<T> = DistanceBasedGlobalStreamer(
            maxCapacity = maxCapacity,
            asyncExecutor = asyncExecutor,
            streamInCandidateSupplier = streamInCandidateSupplier
    )

}