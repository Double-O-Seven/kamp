package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStreamOutListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerStreamOutHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerStreamOutListener>(OnPlayerStreamOutListener::class), OnPlayerStreamOutListener {

    override fun onPlayerStreamOut(player: Player, forPlayer: Player) {
        listeners.forEach {
            it.onPlayerStreamOut(player, forPlayer)
        }
    }

}
