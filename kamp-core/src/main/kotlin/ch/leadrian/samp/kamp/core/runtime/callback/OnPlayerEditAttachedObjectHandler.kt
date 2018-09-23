package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditAttachedObjectListener
import ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.Bone
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEditAttachedObjectHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEditAttachedObjectListener>(OnPlayerEditAttachedObjectListener::class), OnPlayerEditAttachedObjectListener {

    override fun onPlayerEditAttachedObject(player: Player, slot: AttachedObjectSlot, response: AttachedObjectEditResponse, modelId: Int, bone: Bone, offset: Vector3D, rotation: Vector3D, scale: Vector3D) {
        listeners.forEach {
            it.onPlayerEditAttachedObject(player, slot, response, modelId, bone, offset, rotation, scale)
        }
    }

}
