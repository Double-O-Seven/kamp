package ch.leadrian.samp.kamp.streamer.runtime.entity

interface GlobalStreamable : Streamable {

    fun onStreamIn()

    fun onStreamOut()

    val isStreamedIn: Boolean

}