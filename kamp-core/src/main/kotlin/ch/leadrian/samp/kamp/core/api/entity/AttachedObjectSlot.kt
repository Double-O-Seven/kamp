package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.Bone
import ch.leadrian.samp.kamp.core.api.data.AttachedObject
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class AttachedObjectSlot
internal constructor(
        override val player: Player,
        val index: Int,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasPlayer {

    private val onEditHandlers: MutableList<AttachedObjectSlot.(AttachedObjectEditResponse, Int, Bone, Vector3D, Vector3D, Vector3D) -> Boolean> = mutableListOf()

    val isUsed: Boolean
        get() = nativeFunctionExecutor.isPlayerAttachedObjectSlotUsed(playerid = player.id.value, index = index)

    fun remove() {
        nativeFunctionExecutor.removePlayerAttachedObject(playerid = player.id.value, index = index)
        this.attachedObject = null
    }

    fun edit() {
        nativeFunctionExecutor.editAttachedObject(playerid = player.id.value, index = index)
    }

    fun attach(attachedObject: AttachedObject) {
        val success = nativeFunctionExecutor.setPlayerAttachedObject(
                playerid = player.id.value,
                index = index,
                modelid = attachedObject.modelId,
                bone = attachedObject.bone.value,
                fOffsetX = attachedObject.offset.x,
                fOffsetY = attachedObject.offset.y,
                fOffsetZ = attachedObject.offset.z,
                fRotX = attachedObject.rotation.x,
                fRotY = attachedObject.rotation.y,
                fRotZ = attachedObject.rotation.z,
                fScaleX = attachedObject.scale.x,
                fScaleY = attachedObject.scale.y,
                fScaleZ = attachedObject.scale.z,
                materialcolor1 = attachedObject.materialColor1?.value ?: 0,
                materialcolor2 = attachedObject.materialColor2?.value ?: 0
        )
        this.attachedObject = when (success) {
            true -> attachedObject
            else -> null
        }
    }

    var attachedObject: AttachedObject? = null
        private set

    fun onEdit(onEdit: AttachedObjectSlot.(AttachedObjectEditResponse, Int, Bone, Vector3D, Vector3D, Vector3D) -> Boolean) {
        onEditHandlers += onEdit
    }

    internal fun onEdit(
            response: AttachedObjectEditResponse,
            modelId: Int,
            bone: Bone,
            offset: Vector3D,
            rotation: Vector3D,
            scale: Vector3D
    ) {
        onEditHandlers.forEach { it.invoke(this, response, modelId, bone, offset, rotation, scale) }
    }
}