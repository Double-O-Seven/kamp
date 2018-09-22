package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDeathListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerDeathListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerDeathListener>(OnPlayerDeathListener::class), OnPlayerDeathListener {

    override fun onPlayerDeath(player: ch.leadrian.samp.kamp.core.api.entity.Player, killer: ch.leadrian.samp.kamp.core.api.entity.Player?, reason: ch.leadrian.samp.kamp.core.api.constants.WeaponModel): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerDeath(player, killer, reason)
        }
    }

}
