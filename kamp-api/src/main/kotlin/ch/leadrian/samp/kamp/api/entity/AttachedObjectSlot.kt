package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.AttachedObject

interface AttachedObjectSlot {

    val index: Int

    val isUsed: Boolean

    fun remove()

    fun edit()

    fun attach(attachedObject: AttachedObject)

    fun getAttachedObject(): AttachedObject?

}