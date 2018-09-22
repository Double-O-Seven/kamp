package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTextListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerTextListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerTextListener>(OnPlayerTextListener::class), OnPlayerTextListener {

    override fun onPlayerText(player: ch.leadrian.samp.kamp.core.api.entity.Player, text: kotlin.String): ch.leadrian.samp.kamp.core.api.callback.OnPlayerTextListener.Result {
        getListeners().forEach {
            it.onPlayerText(player, text)
        }
    }

}
