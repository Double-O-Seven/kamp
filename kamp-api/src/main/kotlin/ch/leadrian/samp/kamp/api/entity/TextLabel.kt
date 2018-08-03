package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.id.TextLabelId

interface TextLabel : Destroyable {

    val id: TextLabelId

    var text: String

    var color: Color

    val coordinates: Vector3D

    val drawDistance: Float

    val virtualWorldId: Int

    val testLOS: Boolean

    fun update(text: String, color: Color)

    fun attachTo(player: Player, offset: Vector3D)

    fun attachTo(vehicle: Vehicle, offset: Vector3D)
}