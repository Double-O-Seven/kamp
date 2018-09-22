package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditAttachedObjectListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEditAttachedObjectListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEditAttachedObjectListener>(OnPlayerEditAttachedObjectListener::class), OnPlayerEditAttachedObjectListener {

    override fun onPlayerEditAttachedObject(player: ch.leadrian.samp.kamp.core.api.entity.Player, slot: ch.leadrian.samp.kamp.core.api.entity.AttachedObjectSlot, response: ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse, modelId: kotlin.Int, bone: ch.leadrian.samp.kamp.core.api.constants.Bone, offset: ch.leadrian.samp.kamp.core.api.data.Vector3D, rotation: ch.leadrian.samp.kamp.core.api.data.Vector3D, scale: ch.leadrian.samp.kamp.core.api.data.Vector3D): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerEditAttachedObject(player, slot, response, modelId, bone, offset, rotation, scale)
        }
    }

}
