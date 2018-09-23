package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestSpawnListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerRequestSpawnHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerRequestSpawnListener>(OnPlayerRequestSpawnListener::class), OnPlayerRequestSpawnListener {

    override fun onPlayerRequestSpawn(player: Player): Result {
        return listeners.map {
            it.onPlayerRequestSpawn(player)
        }.firstOrNull { it == Result.Denied } ?: Result.Granted
    }

}
