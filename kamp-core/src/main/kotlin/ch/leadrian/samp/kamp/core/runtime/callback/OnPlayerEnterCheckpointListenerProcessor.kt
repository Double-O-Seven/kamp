package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEnterCheckpointListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEnterCheckpointListener>(OnPlayerEnterCheckpointListener::class), OnPlayerEnterCheckpointListener {

    override fun onPlayerEnterCheckpoint(player: Player) {
        listeners.forEach {
            it.onPlayerEnterCheckpoint(player)
        }
    }

}
