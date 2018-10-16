package ch.leadrian.samp.kamp.streamer.entity

interface GlobalStreamable : Streamable {

    fun onStreamIn()

    fun onStreamOut()

    val isStreamedIn: Boolean

}