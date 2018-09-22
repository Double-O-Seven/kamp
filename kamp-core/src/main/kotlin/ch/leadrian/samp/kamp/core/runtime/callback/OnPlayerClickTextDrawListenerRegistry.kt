package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerClickTextDrawListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerClickTextDrawListener>(OnPlayerClickTextDrawListener::class), OnPlayerClickTextDrawListener {

    override fun onPlayerClickTextDraw(player: ch.leadrian.samp.kamp.core.api.entity.Player, textDraw: ch.leadrian.samp.kamp.core.api.entity.TextDraw): ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener.Result {
        getListeners().forEach {
            it.onPlayerClickTextDraw(player, textDraw)
        }
    }

}
