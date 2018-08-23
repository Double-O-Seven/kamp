package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.Bone

data class AttachedObject(
        val modelId: Int,
        val bone: Bone,
        val offset: Vector3D = vector3DOf(0f, 0f, 0f),
        val rotation: Vector3D = vector3DOf(0f, 0f, 0f),
        val scale: Vector3D = vector3DOf(1f, 1f, 1f),
        val materialColor1: Color? = null,
        val materialColor2: Color? = null
)