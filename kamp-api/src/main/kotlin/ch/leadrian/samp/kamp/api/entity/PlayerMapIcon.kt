package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.api.constants.MapIconType
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapIconId

interface PlayerMapIcon : HasPlayer, Destroyable {

    val id: PlayerMapIconId

    var coordinates: Vector3D

    var type: MapIconType

    var color: Color

    var style: MapIconStyle
}