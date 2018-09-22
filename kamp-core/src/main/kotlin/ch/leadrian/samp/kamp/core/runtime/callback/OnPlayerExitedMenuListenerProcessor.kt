package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitedMenuListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerExitedMenuListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerExitedMenuListener>(OnPlayerExitedMenuListener::class), OnPlayerExitedMenuListener {

    override fun onPlayerExitedMenu(player: Player) {
        listeners.forEach {
            it.onPlayerExitedMenu(player)
        }
    }

}
