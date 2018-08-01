package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.api.constants.MapIconType
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D

interface PlayerMapIcon {

    val coordinates: Vector3D

    val type: MapIconType

    val color: Color

    val style: MapIconStyle
}