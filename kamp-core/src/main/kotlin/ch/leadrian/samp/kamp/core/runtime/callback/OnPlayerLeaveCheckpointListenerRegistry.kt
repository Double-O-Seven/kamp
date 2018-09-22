package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveCheckpointListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerLeaveCheckpointListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerLeaveCheckpointListener>(OnPlayerLeaveCheckpointListener::class), OnPlayerLeaveCheckpointListener {

    override fun onPlayerLeaveCheckpoint(player: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onPlayerLeaveCheckpoint(player)
        }
    }

}
