package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerLeaveRaceCheckpointHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerLeaveRaceCheckpointListener>(OnPlayerLeaveRaceCheckpointListener::class), OnPlayerLeaveRaceCheckpointListener {

    override fun onPlayerLeaveRaceCheckpoint(player: Player) {
        listeners.forEach {
            it.onPlayerLeaveRaceCheckpoint(player)
        }
    }

}
