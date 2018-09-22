package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSpawnListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSpawnListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSpawnListener>(OnPlayerSpawnListener::class), OnPlayerSpawnListener {

    override fun onPlayerSpawn(player: ch.leadrian.samp.kamp.core.api.entity.Player): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerSpawn(player)
        }
    }

}
