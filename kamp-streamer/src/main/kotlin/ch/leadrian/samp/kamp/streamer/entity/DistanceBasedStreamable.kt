package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.data.Location

interface DistanceBasedStreamable : Streamable {

    fun distanceTo(location: Location): Float

    val streamDistance: Float

}