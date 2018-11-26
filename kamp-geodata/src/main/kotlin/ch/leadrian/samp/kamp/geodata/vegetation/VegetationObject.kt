package ch.leadrian.samp.kamp.geodata.vegetation

import ch.leadrian.samp.kamp.core.api.data.Vector3D

data class VegetationObject(
        val modelId: Int,
        val coordinates: Vector3D,
        val rotation: Vector3D
)
