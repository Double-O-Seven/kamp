package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEnterRaceCheckpointHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEnterRaceCheckpointListener>(OnPlayerEnterRaceCheckpointListener::class), OnPlayerEnterRaceCheckpointListener {

    override fun onPlayerEnterRaceCheckpoint(player: Player) {
        listeners.forEach {
            it.onPlayerEnterRaceCheckpoint(player)
        }
    }

}
