package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerObjectMovedListener
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerObjectMovedHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerObjectMovedListener>(OnPlayerObjectMovedListener::class), OnPlayerObjectMovedListener {

    override fun onPlayerObjectMoved(playerMapObject: PlayerMapObject) {
        listeners.forEach {
            it.onPlayerObjectMoved(playerMapObject)
        }
    }

}
