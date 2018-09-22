package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectMapObjectListener
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSelectMapObjectListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSelectMapObjectListener>(OnPlayerSelectMapObjectListener::class), OnPlayerSelectMapObjectListener {

    override fun onPlayerSelectMapObject(player: Player, mapObject: MapObject, modelId: Int, coordinates: Vector3D) {
        listeners.forEach {
            it.onPlayerSelectMapObject(player, mapObject, modelId, coordinates)
        }
    }

}
