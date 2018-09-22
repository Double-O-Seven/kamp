package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterRaceCheckpointListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEnterRaceCheckpointListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEnterRaceCheckpointListener>(OnPlayerEnterRaceCheckpointListener::class), OnPlayerEnterRaceCheckpointListener {

    override fun onPlayerEnterRaceCheckpoint(player: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onPlayerEnterRaceCheckpoint(player)
        }
    }

}
