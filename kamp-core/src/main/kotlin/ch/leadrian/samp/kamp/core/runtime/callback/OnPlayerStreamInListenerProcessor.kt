package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStreamInListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerStreamInListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerStreamInListener>(OnPlayerStreamInListener::class), OnPlayerStreamInListener {

    override fun onPlayerStreamIn(player: Player, forPlayer: Player) {
        listeners.forEach {
            it.onPlayerStreamIn(player, forPlayer)
        }
    }

}
