package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

internal class AttachedObjectSlotImpl(
        override val player: Player,
        override val index: Int,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : AttachedObjectSlot {

    private val onEditHandlers: MutableList<AttachedObjectSlot.(ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse, Int, ch.leadrian.samp.kamp.core.api.constants.Bone, Vector3D, Vector3D, Vector3D) -> Boolean> = mutableListOf()

    override val isUsed: Boolean
        get() = nativeFunctionExecutor.isPlayerAttachedObjectSlotUsed(playerid = player.id.value, index = index)

    override fun remove() {
        nativeFunctionExecutor.removePlayerAttachedObject(playerid = player.id.value, index = index)
        this.attachedObject = null
    }

    override fun edit() {
        nativeFunctionExecutor.editAttachedObject(playerid = player.id.value, index = index)
    }

    override fun attach(attachedObject: ch.leadrian.samp.kamp.core.api.data.AttachedObject) {
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

    override var attachedObject: ch.leadrian.samp.kamp.core.api.data.AttachedObject? = null
        private set

    override fun onEdit(onEdit: AttachedObjectSlot.(ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse, Int, ch.leadrian.samp.kamp.core.api.constants.Bone, Vector3D, Vector3D, Vector3D) -> Boolean) {
        onEditHandlers += onEdit
    }

    internal fun onEdit(
            response: ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse,
            modelId: Int,
            bone: ch.leadrian.samp.kamp.core.api.constants.Bone,
            offset: Vector3D,
            rotation: Vector3D,
            scale: Vector3D
    ) {
        onEditHandlers.forEach { it.invoke(this, response, modelId, bone, offset, rotation, scale) }
    }
}