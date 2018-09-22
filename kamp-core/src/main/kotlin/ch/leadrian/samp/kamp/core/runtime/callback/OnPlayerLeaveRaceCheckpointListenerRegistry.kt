package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveRaceCheckpointListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerLeaveRaceCheckpointListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerLeaveRaceCheckpointListener>(OnPlayerLeaveRaceCheckpointListener::class), OnPlayerLeaveRaceCheckpointListener {

    override fun onPlayerLeaveRaceCheckpoint(player: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onPlayerLeaveRaceCheckpoint(player)
        }
    }

}
