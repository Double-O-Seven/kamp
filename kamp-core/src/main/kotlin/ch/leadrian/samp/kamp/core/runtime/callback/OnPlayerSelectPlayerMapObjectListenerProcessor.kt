package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSelectPlayerMapObjectListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSelectPlayerMapObjectListener>(OnPlayerSelectPlayerMapObjectListener::class), OnPlayerSelectPlayerMapObjectListener {

    override fun onPlayerSelectPlayerMapObject(player: Player, playerMapObject: PlayerMapObject, modelId: Int, coordinates: Vector3D) {
        listeners.forEach {
            it.onPlayerSelectPlayerMapObject(player, playerMapObject, modelId, coordinates)
        }
    }

}
