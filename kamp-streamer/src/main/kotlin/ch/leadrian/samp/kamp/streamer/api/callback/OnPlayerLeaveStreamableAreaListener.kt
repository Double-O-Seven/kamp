package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableArea

@CallbackListener("ch.leadrian.samp.kamp.streamer.runtime.callback")
interface OnPlayerLeaveStreamableAreaListener {

    fun onPlayerLeaveStreamableArea(player: Player, area: StreamableArea)

}