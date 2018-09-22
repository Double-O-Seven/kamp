package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerLeaveCheckpointListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerLeaveCheckpointListener>(OnPlayerLeaveCheckpointListener::class), OnPlayerLeaveCheckpointListener {

    override fun onPlayerLeaveCheckpoint(player: Player) {
        listeners.forEach {
            it.onPlayerLeaveCheckpoint(player)
        }
    }

}
