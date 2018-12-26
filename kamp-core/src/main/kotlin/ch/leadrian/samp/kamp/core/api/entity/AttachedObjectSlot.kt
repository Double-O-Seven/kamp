package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.AttachedObject
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class AttachedObjectSlot
internal constructor(
        override val player: Player,
        val index: Int,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : HasPlayer {

    var attachedObject: AttachedObject? = null
        private set

    val isUsed: Boolean
        get() = nativeFunctionExecutor.isPlayerAttachedObjectSlotUsed(playerid = player.id.value, index = index)

    fun remove() {
        nativeFunctionExecutor.removePlayerAttachedObject(playerid = player.id.value, index = index)
        attachedObject = null
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
        this.attachedObject = if (success == true) {
            attachedObject
        } else {
            null
        }
    }

}