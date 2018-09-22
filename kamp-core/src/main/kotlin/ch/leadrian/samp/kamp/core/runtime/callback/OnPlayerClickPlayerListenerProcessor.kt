package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerListener.Result
import ch.leadrian.samp.kamp.core.api.constants.ClickPlayerSource
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerClickPlayerListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerClickPlayerListener>(OnPlayerClickPlayerListener::class), OnPlayerClickPlayerListener {

    override fun onPlayerClickPlayer(player: Player, clickedPlayer: Player, source: ClickPlayerSource): Result {
        return listeners.map {
            it.onPlayerClickPlayer(player, clickedPlayer, source)
        }.firstOrNull { it == Result.Processed } ?: Result.Continue
    }

}
