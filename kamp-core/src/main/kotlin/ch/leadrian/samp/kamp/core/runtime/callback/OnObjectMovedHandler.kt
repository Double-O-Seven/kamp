package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnObjectMovedListener
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnObjectMovedHandler
@Inject
constructor() : CallbackListenerRegistry<OnObjectMovedListener>(OnObjectMovedListener::class), OnObjectMovedListener {

    override fun onObjectMoved(mapObject: MapObject) {
        listeners.forEach {
            it.onObjectMoved(mapObject)
        }
    }

}
