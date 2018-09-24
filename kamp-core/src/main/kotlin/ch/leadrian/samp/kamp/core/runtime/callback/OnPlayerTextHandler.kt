package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTextListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTextListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerTextHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerTextListener>(OnPlayerTextListener::class), OnPlayerTextListener {

    override fun onPlayerText(player: Player, text: String): Result {
        return listeners.map {
            it.onPlayerText(player, text)
        }.firstOrNull { it == Result.Blocked } ?: Result.Allowed
    }

}
