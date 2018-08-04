package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.PlayerMapObject

interface OnPlayerEditPlayerMapObjectListener {

    fun onPlayerEditPlayerMapObject(
            mapObject: PlayerMapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    ): Boolean

}
