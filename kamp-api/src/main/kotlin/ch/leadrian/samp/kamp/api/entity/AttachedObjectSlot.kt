package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.api.constants.Bone
import ch.leadrian.samp.kamp.api.data.AttachedObject
import ch.leadrian.samp.kamp.api.data.Vector3D

interface AttachedObjectSlot : HasPlayer {

    val index: Int

    val isUsed: Boolean

    fun remove()

    fun edit()

    fun attach(attachedObject: AttachedObject)

    fun getAttachedObject(): AttachedObject?

    fun onEdit(onEdit: AttachedObjectSlot.(AttachedObjectEditResponse, Int, Bone, Vector3D, Vector3D, Vector3D) -> Boolean)

}