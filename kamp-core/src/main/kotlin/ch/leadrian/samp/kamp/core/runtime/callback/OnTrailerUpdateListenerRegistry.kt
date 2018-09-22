package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnTrailerUpdateListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnTrailerUpdateListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnTrailerUpdateListener>(OnTrailerUpdateListener::class), OnTrailerUpdateListener {

    override fun onTrailerUpdate(player: ch.leadrian.samp.kamp.core.api.entity.Player, vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle): ch.leadrian.samp.kamp.core.api.callback.OnTrailerUpdateListener.Result {
        getListeners().forEach {
            it.onTrailerUpdate(player, vehicle)
        }
    }

}
