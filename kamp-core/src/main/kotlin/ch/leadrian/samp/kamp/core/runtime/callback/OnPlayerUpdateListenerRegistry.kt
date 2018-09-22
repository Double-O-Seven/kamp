package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerUpdateListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerUpdateListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerUpdateListener>(OnPlayerUpdateListener::class), OnPlayerUpdateListener {

    override fun onPlayerUpdate(player: ch.leadrian.samp.kamp.core.api.entity.Player): ch.leadrian.samp.kamp.core.api.callback.OnPlayerUpdateListener.Result {
        getListeners().forEach {
            it.onPlayerUpdate(player)
        }
    }

}
