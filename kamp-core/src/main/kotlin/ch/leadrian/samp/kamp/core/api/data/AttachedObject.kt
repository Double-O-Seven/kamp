package ch.leadrian.samp.kamp.core.api.data

data class AttachedObject(
        val modelId: Int,
        val bone: ch.leadrian.samp.kamp.core.api.constants.Bone,
        val offset: Vector3D = vector3DOf(0f, 0f, 0f),
        val rotation: Vector3D = vector3DOf(0f, 0f, 0f),
        val scale: Vector3D = vector3DOf(1f, 1f, 1f),
        val materialColor1: ch.leadrian.samp.kamp.core.api.data.Color? = null,
        val materialColor2: ch.leadrian.samp.kamp.core.api.data.Color? = null
)