package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerObjectMovedListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerMapObjectCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnPlayerObjectMovedListener, OnPlayerEditPlayerMapObjectListener, OnPlayerSelectPlayerMapObjectListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerEditPlayerMapObject(
            playerMapObject: PlayerMapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    ) {
        playerMapObject.onEdit(response = response, offset = offset, rotation = rotation)
    }

    override fun onPlayerObjectMoved(playerMapObject: PlayerMapObject) {
        playerMapObject.onMoved()
    }

    override fun onPlayerSelectPlayerMapObject(playerMapObject: PlayerMapObject, modelId: Int, coordinates: Vector3D) {
        playerMapObject.onSelect(modelId, coordinates)
    }
}