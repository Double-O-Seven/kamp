package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableRaceCheckpoint

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.streamer.runtime.callback")
interface OnStreamableRaceCheckpointStreamInListener {

    @InlineCallback("onStreamIn")
    fun onStreamableRaceCheckpointStreamIn(@Receiver streamableRaceCheckpoint: StreamableRaceCheckpoint, forPlayer: Player)

}
