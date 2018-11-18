package ch.leadrian.samp.kamp.streamer.api

import ch.leadrian.samp.kamp.streamer.api.entity.StreamLocation

interface Streamer {

    fun stream(streamLocations: List<StreamLocation>)

}