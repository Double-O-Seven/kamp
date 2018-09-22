package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerObjectMovedListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerObjectMovedListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerObjectMovedListener>(OnPlayerObjectMovedListener::class), OnPlayerObjectMovedListener {

    override fun onPlayerObjectMoved(playerMapObject: ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject) {
        getListeners().forEach {
            it.onPlayerObjectMoved(playerMapObject)
        }
    }

}
