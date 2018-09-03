package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.id.PlayerTextLabelId

interface PlayerTextLabel : HasPlayer, Destroyable, Entity<PlayerTextLabelId> {

    override val id: PlayerTextLabelId

    var text: String

    var color: Color

    val coordinates: Vector3D

    val drawDistance: Float

    val testLOS: Boolean

    fun update(text: String, color: Color)

}