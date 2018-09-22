package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDeathListener
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerDeathListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerDeathListener>(OnPlayerDeathListener::class), OnPlayerDeathListener {

    override fun onPlayerDeath(player: Player, killer: Player?, reason: WeaponModel) {
        listeners.forEach {
            it.onPlayerDeath(player, killer, reason)
        }
    }

}
