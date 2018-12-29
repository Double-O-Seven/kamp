package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnMapObjectMovedListener
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnMapObjectMovedHandler
@Inject
constructor() : CallbackListenerRegistry<OnMapObjectMovedListener>(OnMapObjectMovedListener::class), OnMapObjectMovedListener {

    override fun onMapObjectMoved(mapObject: MapObject) {
        listeners.forEach {
            it.onMapObjectMoved(mapObject)
        }
    }

}
