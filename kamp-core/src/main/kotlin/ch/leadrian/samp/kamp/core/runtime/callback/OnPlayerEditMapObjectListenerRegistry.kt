package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditMapObjectListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEditMapObjectListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEditMapObjectListener>(OnPlayerEditMapObjectListener::class), OnPlayerEditMapObjectListener {

    override fun onPlayerEditMapObject(player: ch.leadrian.samp.kamp.core.api.entity.Player, mapObject: ch.leadrian.samp.kamp.core.api.entity.MapObject, response: ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse, offset: ch.leadrian.samp.kamp.core.api.data.Vector3D, rotation: ch.leadrian.samp.kamp.core.api.data.Vector3D): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerEditMapObject(player, mapObject, response, offset, rotation)
        }
    }

}
