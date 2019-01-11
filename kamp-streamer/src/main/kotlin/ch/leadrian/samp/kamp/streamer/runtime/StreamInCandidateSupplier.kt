package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.streamer.api.entity.Streamable
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation
import java.util.stream.Stream

interface StreamInCandidateSupplier<T : Streamable> {

    fun getStreamInCandidates(streamLocation: StreamLocation): Stream<T>

}