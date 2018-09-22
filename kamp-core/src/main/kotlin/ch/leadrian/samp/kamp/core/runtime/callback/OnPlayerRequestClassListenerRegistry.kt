package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestClassListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerRequestClassListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerRequestClassListener>(OnPlayerRequestClassListener::class), OnPlayerRequestClassListener {

    override fun onPlayerRequestClass(player: ch.leadrian.samp.kamp.core.api.entity.Player, playerClass: ch.leadrian.samp.kamp.core.api.entity.PlayerClass) {
        getListeners().forEach {
            it.onPlayerRequestClass(player, playerClass)
        }
    }

}
