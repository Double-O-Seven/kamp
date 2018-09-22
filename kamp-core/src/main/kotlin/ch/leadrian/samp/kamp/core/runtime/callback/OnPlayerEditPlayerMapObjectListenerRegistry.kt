package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditPlayerMapObjectListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEditPlayerMapObjectListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEditPlayerMapObjectListener>(OnPlayerEditPlayerMapObjectListener::class), OnPlayerEditPlayerMapObjectListener {

    override fun onPlayerEditPlayerMapObject(mapObject: ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject, response: ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse, offset: ch.leadrian.samp.kamp.core.api.data.Vector3D, rotation: ch.leadrian.samp.kamp.core.api.data.Vector3D): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerEditPlayerMapObject(mapObject, response, offset, rotation)
        }
    }

}
