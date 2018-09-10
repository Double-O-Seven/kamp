package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject

interface OnPlayerSelectPlayerMapObjectListener {

    fun onPlayerSelectPlayerMapObject(player: Player, playerMapObject: PlayerMapObject, modelId: Int, coordinates: Vector3D): Boolean

}
