package ch.leadrian.samp.kamp.streamer.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerSelectStreamableMapObjectListener
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSelectStreamableMapObjectHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSelectStreamableMapObjectListener>(OnPlayerSelectStreamableMapObjectListener::class), OnPlayerSelectStreamableMapObjectListener {

    override fun onPlayerSelectStreamableMapObject(player: Player, streamableMapObject: StreamableMapObject, modelId: Int, coordinates: Vector3D) {
        listeners.forEach {
            it.onPlayerSelectStreamableMapObject(player, streamableMapObject, modelId, coordinates)
        }
    }

}
