package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerInteriorChangeListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerInteriorChangeListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerInteriorChangeListener>(OnPlayerInteriorChangeListener::class), OnPlayerInteriorChangeListener {

    override fun onPlayerInteriorChange(player: ch.leadrian.samp.kamp.core.api.entity.Player, newInteriorId: kotlin.Int, oldInteriorId: kotlin.Int) {
        getListeners().forEach {
            it.onPlayerInteriorChange(player, newInteriorId, oldInteriorId)
        }
    }

}
