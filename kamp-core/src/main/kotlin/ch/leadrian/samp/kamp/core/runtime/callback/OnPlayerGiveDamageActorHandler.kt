package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageActorListener
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerGiveDamageActorHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerGiveDamageActorListener>(OnPlayerGiveDamageActorListener::class), OnPlayerGiveDamageActorListener {

    override fun onPlayerGiveDamageActor(player: Player, actor: Actor, amount: Float, weaponModel: WeaponModel, bodyPart: BodyPart) {
        listeners.forEach {
            it.onPlayerGiveDamageActor(player, actor, amount, weaponModel, bodyPart)
        }
    }

}
