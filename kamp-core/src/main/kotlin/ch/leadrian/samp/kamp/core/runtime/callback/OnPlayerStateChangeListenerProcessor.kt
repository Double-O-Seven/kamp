package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStateChangeListener
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerStateChangeListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerStateChangeListener>(OnPlayerStateChangeListener::class), OnPlayerStateChangeListener {

    override fun onPlayerStateChange(player: Player, newState: PlayerState, oldState: PlayerState) {
        listeners.forEach {
            it.onPlayerStateChange(player, newState, oldState)
        }
    }

}
