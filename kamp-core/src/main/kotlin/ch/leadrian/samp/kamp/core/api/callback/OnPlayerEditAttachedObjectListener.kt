package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerEditAttachedObjectListener {

    fun onPlayerEditAttachedObject(
            player: Player,
            slot: AttachedObjectSlot,
            response: ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse,
            modelId: Int,
            bone: ch.leadrian.samp.kamp.core.api.constants.Bone,
            offset: Vector3D,
            rotation: Vector3D,
            scale: Vector3D
    )

}
