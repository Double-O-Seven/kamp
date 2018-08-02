package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.Bone

data class AttachedObject(
        val modelId: Int,
        val bone: Bone,
        val offset: Vector3D,
        val rotation: Vector3D,
        val scale: Vector3D,
        val materialColor1: Color = colorOf(0),
        val materialColor2: Color = colorOf(0)
)