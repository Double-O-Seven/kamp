package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStreamOutListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerStreamOutListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerStreamOutListener>(OnPlayerStreamOutListener::class), OnPlayerStreamOutListener {

    override fun onPlayerStreamOut(player: ch.leadrian.samp.kamp.core.api.entity.Player, forPlayer: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onPlayerStreamOut(player, forPlayer)
        }
    }

}
