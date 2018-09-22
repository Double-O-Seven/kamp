package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStreamInListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerStreamInListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerStreamInListener>(OnPlayerStreamInListener::class), OnPlayerStreamInListener {

    override fun onPlayerStreamIn(player: ch.leadrian.samp.kamp.core.api.entity.Player, forPlayer: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onPlayerStreamIn(player, forPlayer)
        }
    }

}
