package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitedMenuListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerExitedMenuListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerExitedMenuListener>(OnPlayerExitedMenuListener::class), OnPlayerExitedMenuListener {

    override fun onPlayerExitedMenu(player: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onPlayerExitedMenu(player)
        }
    }

}
