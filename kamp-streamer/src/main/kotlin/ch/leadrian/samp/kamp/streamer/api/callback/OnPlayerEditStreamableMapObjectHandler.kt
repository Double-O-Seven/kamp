package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEditStreamableMapObjectHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEditStreamableMapObjectListener>(OnPlayerEditStreamableMapObjectListener::class), OnPlayerEditStreamableMapObjectListener {

    override fun onPlayerEditStreamableMapObject(
            player: Player,
            streamableMapObject: StreamableMapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    ) {
        listeners.forEach {
            it.onPlayerEditStreamableMapObject(player, streamableMapObject, response, offset, rotation)
        }
    }

}
