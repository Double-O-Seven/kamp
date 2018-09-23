package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerUpdateListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerUpdateHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerUpdateListener>(OnPlayerUpdateListener::class), OnPlayerUpdateListener {

    override fun onPlayerUpdate(player: Player): Result {
        return listeners.map {
            it.onPlayerUpdate(player)
        }.firstOrNull { it == Result.Desync } ?: Result.Sync
    }

}
