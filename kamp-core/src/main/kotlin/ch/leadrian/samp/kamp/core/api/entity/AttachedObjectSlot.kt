package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D

interface AttachedObjectSlot : HasPlayer {

    val index: Int

    val isUsed: Boolean

    fun remove()

    fun edit()

    fun attach(attachedObject: ch.leadrian.samp.kamp.core.api.data.AttachedObject)

    val attachedObject: ch.leadrian.samp.kamp.core.api.data.AttachedObject?

    fun onEdit(onEdit: AttachedObjectSlot.(ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse, Int, ch.leadrian.samp.kamp.core.api.constants.Bone, Vector3D, Vector3D, Vector3D) -> Boolean)

}