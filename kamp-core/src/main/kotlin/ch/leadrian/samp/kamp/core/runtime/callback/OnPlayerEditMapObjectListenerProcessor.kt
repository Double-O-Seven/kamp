package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditMapObjectListener
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEditMapObjectListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEditMapObjectListener>(OnPlayerEditMapObjectListener::class), OnPlayerEditMapObjectListener {

    override fun onPlayerEditMapObject(player: Player, mapObject: MapObject, response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        listeners.forEach {
            it.onPlayerEditMapObject(player, mapObject, response, offset, rotation)
        }
    }

}
