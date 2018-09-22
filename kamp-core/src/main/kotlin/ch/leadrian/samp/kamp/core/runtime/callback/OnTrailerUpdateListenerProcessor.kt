package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnTrailerUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnTrailerUpdateListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnTrailerUpdateListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnTrailerUpdateListener>(OnTrailerUpdateListener::class), OnTrailerUpdateListener {

    override fun onTrailerUpdate(player: Player, vehicle: Vehicle): Result {
        return listeners.map {
            it.onTrailerUpdate(player, vehicle)
        }.firstOrNull { it == Result.Desync } ?: Result.Sync
    }

}
