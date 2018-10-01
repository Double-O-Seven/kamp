package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RaceCheckpointCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnPlayerEnterRaceCheckpointListener, OnPlayerLeaveRaceCheckpointListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerEnterRaceCheckpoint(player: Player) {
        player.raceCheckpoint?.onEnter(player)
    }

    override fun onPlayerLeaveRaceCheckpoint(player: Player) {
        player.raceCheckpoint?.onLeave(player)
    }

}