package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerClickPlayerListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerClickPlayerListener>(OnPlayerClickPlayerListener::class), OnPlayerClickPlayerListener {

    override fun onPlayerClickPlayer(player: ch.leadrian.samp.kamp.core.api.entity.Player, clickedPlayer: ch.leadrian.samp.kamp.core.api.entity.Player, source: ch.leadrian.samp.kamp.core.api.constants.ClickPlayerSource): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerClickPlayer(player, clickedPlayer, source)
        }
    }

}
