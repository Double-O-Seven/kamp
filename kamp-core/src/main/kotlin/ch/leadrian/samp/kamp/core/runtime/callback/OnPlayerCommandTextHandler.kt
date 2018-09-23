package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerCommandTextHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerCommandTextListener>(OnPlayerCommandTextListener::class), OnPlayerCommandTextListener {

    override fun onPlayerCommandText(player: Player, commandText: String): Result {
        return listeners.map {
            it.onPlayerCommandText(player, commandText)
        }.firstOrNull { it == Result.Processed } ?: Result.UnknownCommand
    }

}
