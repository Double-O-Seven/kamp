package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerNameChangeListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerNameChangeHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerNameChangeListener>(OnPlayerNameChangeListener::class), OnPlayerNameChangeListener {

    override fun onPlayerNameChange(player: Player, oldName: String, newName: String) {
        listeners.forEach {
            it.onPlayerNameChange(player, oldName, newName)
        }
    }

}
