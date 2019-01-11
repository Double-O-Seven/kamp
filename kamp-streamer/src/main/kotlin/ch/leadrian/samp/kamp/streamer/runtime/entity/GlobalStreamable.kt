package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.streamer.api.entity.Streamable

interface GlobalStreamable : Streamable {

    fun onStreamIn()

    fun onStreamOut()

    val isStreamedIn: Boolean

}