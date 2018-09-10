package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextLabelId

interface PlayerTextLabel : HasPlayer, Destroyable, Entity<PlayerTextLabelId> {

    override val id: PlayerTextLabelId

    var text: String

    var color: ch.leadrian.samp.kamp.core.api.data.Color

    val coordinates: Vector3D

    val drawDistance: Float

    val testLOS: Boolean

    fun update(text: String, color: ch.leadrian.samp.kamp.core.api.data.Color)

}