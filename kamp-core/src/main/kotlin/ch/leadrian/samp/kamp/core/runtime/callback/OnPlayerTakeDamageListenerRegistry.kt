package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTakeDamageListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerTakeDamageListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerTakeDamageListener>(OnPlayerTakeDamageListener::class), OnPlayerTakeDamageListener {

    override fun onPlayerTakeDamage(player: ch.leadrian.samp.kamp.core.api.entity.Player, issuer: ch.leadrian.samp.kamp.core.api.entity.Player?, amount: kotlin.Float, weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, bodyPart: ch.leadrian.samp.kamp.core.api.constants.BodyPart): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerTakeDamage(player, issuer, amount, weaponModel, bodyPart)
        }
    }

}
