package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerSelectMapObjectListener {

    fun onPlayerSelectMapObject(player: Player, mapObject: MapObject, modelId: Int, coordinates: Vector3D)

}
