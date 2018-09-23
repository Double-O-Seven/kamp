package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTakeDamageListener
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerTakeDamageHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerTakeDamageListener>(OnPlayerTakeDamageListener::class), OnPlayerTakeDamageListener {

    override fun onPlayerTakeDamage(player: Player, issuer: Player?, amount: Float, weaponModel: WeaponModel, bodyPart: BodyPart) {
        listeners.forEach {
            it.onPlayerTakeDamage(player, issuer, amount, weaponModel, bodyPart)
        }
    }

}
