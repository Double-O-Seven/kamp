package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestSpawnListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerRequestSpawnListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerRequestSpawnListener>(OnPlayerRequestSpawnListener::class), OnPlayerRequestSpawnListener {

    override fun onPlayerRequestSpawn(player: ch.leadrian.samp.kamp.core.api.entity.Player): ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestSpawnListener.Result {
        getListeners().forEach {
            it.onPlayerRequestSpawn(player)
        }
    }

}
