package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D

interface TextLabelBase : Destroyable {

    val drawDistance: Float

    val testLOS: Boolean

    var text: String

    var color: Color

    val coordinates: Vector3D

    fun update(text: String, color: Color)
}