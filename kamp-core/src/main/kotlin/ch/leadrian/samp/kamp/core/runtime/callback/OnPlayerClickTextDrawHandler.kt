package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerClickTextDrawHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerClickTextDrawListener>(OnPlayerClickTextDrawListener::class), OnPlayerClickTextDrawListener {

    override fun onPlayerClickTextDraw(player: Player, textDraw: TextDraw): Result {
        return listeners.map {
            it.onPlayerClickTextDraw(player, textDraw)
        }.firstOrNull { it == Result.Processed } ?: Result.NotFound
    }

}
