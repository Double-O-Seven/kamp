package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamLocation

interface Streamer {

    fun stream(streamLocations: List<StreamLocation>)

}