package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterCheckpointListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEnterCheckpointListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEnterCheckpointListener>(OnPlayerEnterCheckpointListener::class), OnPlayerEnterCheckpointListener {

    override fun onPlayerEnterCheckpoint(player: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onPlayerEnterCheckpoint(player)
        }
    }

}
