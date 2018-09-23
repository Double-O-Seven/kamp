package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSpawnListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSpawnHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSpawnListener>(OnPlayerSpawnListener::class), OnPlayerSpawnListener {

    override fun onPlayerSpawn(player: Player) {
        listeners.forEach {
            it.onPlayerSpawn(player)
        }
    }

}
