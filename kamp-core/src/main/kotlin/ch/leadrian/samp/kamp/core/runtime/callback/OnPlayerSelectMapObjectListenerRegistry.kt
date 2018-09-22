package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectMapObjectListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSelectMapObjectListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSelectMapObjectListener>(OnPlayerSelectMapObjectListener::class), OnPlayerSelectMapObjectListener {

    override fun onPlayerSelectMapObject(player: ch.leadrian.samp.kamp.core.api.entity.Player, mapObject: ch.leadrian.samp.kamp.core.api.entity.MapObject, modelId: kotlin.Int, coordinates: ch.leadrian.samp.kamp.core.api.data.Vector3D): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerSelectMapObject(player, mapObject, modelId, coordinates)
        }
    }

}
