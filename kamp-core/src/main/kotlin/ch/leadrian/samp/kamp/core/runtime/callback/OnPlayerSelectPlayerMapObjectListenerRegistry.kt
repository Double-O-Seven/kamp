package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectPlayerMapObjectListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSelectPlayerMapObjectListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSelectPlayerMapObjectListener>(OnPlayerSelectPlayerMapObjectListener::class), OnPlayerSelectPlayerMapObjectListener {

    override fun onPlayerSelectPlayerMapObject(player: ch.leadrian.samp.kamp.core.api.entity.Player, playerMapObject: ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject, modelId: kotlin.Int, coordinates: ch.leadrian.samp.kamp.core.api.data.Vector3D): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerSelectPlayerMapObject(player, playerMapObject, modelId, coordinates)
        }
    }

}
