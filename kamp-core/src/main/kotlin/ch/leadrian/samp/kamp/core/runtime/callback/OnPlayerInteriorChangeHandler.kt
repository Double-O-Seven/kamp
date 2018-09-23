package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerInteriorChangeListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerInteriorChangeHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerInteriorChangeListener>(OnPlayerInteriorChangeListener::class), OnPlayerInteriorChangeListener {

    override fun onPlayerInteriorChange(player: Player, newInteriorId: Int, oldInteriorId: Int) {
        listeners.forEach {
            it.onPlayerInteriorChange(player, newInteriorId, oldInteriorId)
        }
    }

}
