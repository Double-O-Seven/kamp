package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableActor

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.streamer.runtime.callback")
interface OnPlayerDamageStreamableActorListener {

    @InlineCallback("onDamage")
    fun onPlayerDamageStreamableActor(
            player: Player,
            @Receiver streamableActor: StreamableActor,
            amount: Float,
            weaponModel: WeaponModel,
            bodyPart: BodyPart
    )

}
