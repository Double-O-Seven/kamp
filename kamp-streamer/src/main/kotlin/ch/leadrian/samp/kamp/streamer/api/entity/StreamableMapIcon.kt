package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player

interface StreamableMapIcon : Streamable {

    var coordinates: Vector3D

    var type: MapIconType

    var color: Color

    var style: MapIconStyle

    val virtualWorldIds: MutableSet<Int>

    val interiorIds: MutableSet<Int>

    fun isStreamedIn(forPlayer: Player): Boolean

    fun isVisible(forPlayer: Player): Boolean

    fun visibleWhen(condition: StreamableMapIcon.(Player) -> Boolean)

}