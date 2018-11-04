package ch.leadrian.samp.kamp.streamer.api

import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation

interface Streamer {

    fun stream(streamLocations: List<StreamLocation>)

}