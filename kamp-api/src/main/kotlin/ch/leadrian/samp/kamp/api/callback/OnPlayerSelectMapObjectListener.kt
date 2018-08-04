package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.MapObject
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerSelectMapObjectListener {

    fun onPlayerSelectMapObject(player: Player, mapObject: MapObject, modelId: Int, coordinates: Vector3D): Boolean

}
