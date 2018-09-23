package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEditPlayerMapObjectHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEditPlayerMapObjectListener>(OnPlayerEditPlayerMapObjectListener::class), OnPlayerEditPlayerMapObjectListener {

    override fun onPlayerEditPlayerMapObject(mapObject: PlayerMapObject, response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        listeners.forEach {
            it.onPlayerEditPlayerMapObject(mapObject, response, offset, rotation)
        }
    }

}
