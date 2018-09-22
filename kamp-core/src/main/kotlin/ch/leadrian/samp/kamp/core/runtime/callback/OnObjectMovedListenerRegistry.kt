package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnObjectMovedListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnObjectMovedListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnObjectMovedListener>(OnObjectMovedListener::class), OnObjectMovedListener {

    override fun onObjectMoved(mapObject: ch.leadrian.samp.kamp.core.api.entity.MapObject) {
        getListeners().forEach {
            it.onObjectMoved(mapObject)
        }
    }

}
