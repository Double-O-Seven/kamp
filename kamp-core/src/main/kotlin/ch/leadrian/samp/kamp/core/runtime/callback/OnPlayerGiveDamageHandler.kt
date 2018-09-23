package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageListener
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerGiveDamageHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerGiveDamageListener>(OnPlayerGiveDamageListener::class), OnPlayerGiveDamageListener {

    override fun onPlayerGiveDamage(player: Player, damagedPlayer: Player, amount: Float, weaponModel: WeaponModel, bodyPart: BodyPart) {
        listeners.forEach {
            it.onPlayerGiveDamage(player, damagedPlayer, amount, weaponModel, bodyPart)
        }
    }

}
