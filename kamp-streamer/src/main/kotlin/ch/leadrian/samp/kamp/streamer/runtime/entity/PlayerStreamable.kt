package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.Streamable

interface PlayerStreamable : Streamable {

    fun onStreamIn(forPlayer: Player)

    fun onStreamOut(forPlayer: Player)

    fun isStreamedIn(forPlayer: Player): Boolean

    fun isVisible(forPlayer: Player): Boolean

}