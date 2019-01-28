package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamablePickup

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.streamer.runtime.callback")
interface OnStreamablePickupStreamInListener {

    @InlineCallback("onStreamIn")
    fun onStreamablePickupStreamIn(@Receiver streamablePickup: StreamablePickup)

}
