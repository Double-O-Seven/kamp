package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId

interface PlayerMapIcon : HasPlayer, Destroyable, Entity<PlayerMapIconId> {

    override val id: PlayerMapIconId

    var coordinates: Vector3D

    var type: ch.leadrian.samp.kamp.core.api.constants.MapIconType

    var color: ch.leadrian.samp.kamp.core.api.data.Color

    var style: ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
}