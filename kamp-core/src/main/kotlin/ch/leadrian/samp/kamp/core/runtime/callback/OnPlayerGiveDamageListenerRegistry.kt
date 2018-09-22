package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerGiveDamageListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerGiveDamageListener>(OnPlayerGiveDamageListener::class), OnPlayerGiveDamageListener {

    override fun onPlayerGiveDamage(player: ch.leadrian.samp.kamp.core.api.entity.Player, damagedPlayer: ch.leadrian.samp.kamp.core.api.entity.Player, amount: kotlin.Float, weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, bodyPart: ch.leadrian.samp.kamp.core.api.constants.BodyPart): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerGiveDamage(player, damagedPlayer, amount, weaponModel, bodyPart)
        }
    }

}
