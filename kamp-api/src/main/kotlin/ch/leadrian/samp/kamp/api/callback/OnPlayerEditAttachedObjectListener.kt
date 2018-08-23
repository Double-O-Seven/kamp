package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.api.constants.Bone
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerEditAttachedObjectListener {

    fun onPlayerEditAttachedObject(
            player: Player,
            slot: AttachedObjectSlot,
            response: AttachedObjectEditResponse,
            modelId: Int,
            bone: Bone,
            offset: Vector3D,
            rotation: Vector3D,
            scale: Vector3D
    ): Boolean

}
