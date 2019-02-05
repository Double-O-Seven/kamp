package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEnterStreamableAreaReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerLeaveStreamableAreaReceiver

interface StreamableArea :
        Streamable,
        OnPlayerEnterStreamableAreaReceiver,
        OnPlayerLeaveStreamableAreaReceiver {

    var interiorIds: MutableSet<Int>

    var virtualWorldIds: MutableSet<Int>

    fun contains(player: Player): Boolean

}
