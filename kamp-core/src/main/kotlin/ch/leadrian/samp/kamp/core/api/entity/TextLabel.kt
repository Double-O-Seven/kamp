package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.TextLabelId

interface TextLabel : Destroyable, Entity<TextLabelId> {

    override val id: TextLabelId

    var text: String

    var color: ch.leadrian.samp.kamp.core.api.data.Color

    val coordinates: Vector3D

    val drawDistance: Float

    val virtualWorldId: Int

    val testLOS: Boolean

    fun update(text: String, color: ch.leadrian.samp.kamp.core.api.data.Color)

    fun attachTo(player: Player, offset: Vector3D)

    fun attachTo(vehicle: Vehicle, offset: Vector3D)
}