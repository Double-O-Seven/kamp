package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerCommandTextListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerCommandTextListener>(OnPlayerCommandTextListener::class), OnPlayerCommandTextListener {

    override fun onPlayerCommandText(player: ch.leadrian.samp.kamp.core.api.entity.Player, commandText: kotlin.String): ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener.Result {
        getListeners().forEach {
            it.onPlayerCommandText(player, commandText)
        }
    }

}
