package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.api.constants.Bone
import ch.leadrian.samp.kamp.api.data.AttachedObject
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor

internal class AttachedObjectSlotImpl(
        override val player: Player,
        override val index: Int,
        private val nativeFunctionsExecutor: SAMPNativeFunctionExecutor
) : AttachedObjectSlot {

    private val onEditHandlers: MutableList<AttachedObjectSlot.(AttachedObjectEditResponse, Int, Bone, Vector3D, Vector3D, Vector3D) -> Boolean> = mutableListOf()

    override val isUsed: Boolean
        get() = nativeFunctionsExecutor.isPlayerAttachedObjectSlotUsed(playerid = player.id.value, index = index)

    override fun remove() {
        nativeFunctionsExecutor.removePlayerAttachedObject(playerid = player.id.value, index = index)
        this.attachedObject = null
    }

    override fun edit() {
        nativeFunctionsExecutor.editAttachedObject(playerid = player.id.value, index = index)
    }

    override fun attach(attachedObject: AttachedObject) {
        val success = nativeFunctionsExecutor.setPlayerAttachedObject(
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

    override var attachedObject: AttachedObject? = null
        private set

    override fun onEdit(onEdit: AttachedObjectSlot.(AttachedObjectEditResponse, Int, Bone, Vector3D, Vector3D, Vector3D) -> Boolean) {
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