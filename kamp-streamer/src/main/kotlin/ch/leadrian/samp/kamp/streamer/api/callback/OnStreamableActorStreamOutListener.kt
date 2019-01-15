package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableActor

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.streamer.runtime.callback")
interface OnStreamableActorStreamOutListener {

    @InlineCallback("onStreamOut")
    fun onStreamableActorStreamOut(@Receiver streamableActor: StreamableActor, forPlayer: Player)

}
