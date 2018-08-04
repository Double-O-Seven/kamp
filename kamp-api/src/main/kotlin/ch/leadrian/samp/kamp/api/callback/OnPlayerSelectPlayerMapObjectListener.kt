package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.PlayerMapObject

interface OnPlayerSelectPlayerMapObjectListener {

    fun onPlayerSelectPlayerMapObject(player: Player, playerMapObject: PlayerMapObject, modelId: Int, coordinates: Vector3D): Boolean

}
