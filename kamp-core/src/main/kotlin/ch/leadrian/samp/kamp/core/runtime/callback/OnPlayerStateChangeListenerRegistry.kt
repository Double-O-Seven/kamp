package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStateChangeListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerStateChangeListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerStateChangeListener>(OnPlayerStateChangeListener::class), OnPlayerStateChangeListener {

    override fun onPlayerStateChange(player: ch.leadrian.samp.kamp.core.api.entity.Player, newState: ch.leadrian.samp.kamp.core.api.constants.PlayerState, oldState: ch.leadrian.samp.kamp.core.api.constants.PlayerState) {
        getListeners().forEach {
            it.onPlayerStateChange(player, newState, oldState)
        }
    }

}
