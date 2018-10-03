package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject

interface OnPlayerSelectPlayerMapObjectListener {

    fun onPlayerSelectPlayerMapObject(playerMapObject: PlayerMapObject, modelId: Int, coordinates: Vector3D)

}
