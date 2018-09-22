package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageActorListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerGiveDamageActorListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerGiveDamageActorListener>(OnPlayerGiveDamageActorListener::class), OnPlayerGiveDamageActorListener {

    override fun onPlayerGiveDamageActor(player: ch.leadrian.samp.kamp.core.api.entity.Player, actor: ch.leadrian.samp.kamp.core.api.entity.Actor, amount: kotlin.Float, weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, bodyPart: ch.leadrian.samp.kamp.core.api.constants.BodyPart): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerGiveDamageActor(player, actor, amount, weaponModel, bodyPart)
        }
    }

}
