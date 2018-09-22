package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject

interface OnPlayerEditPlayerMapObjectListener {

    fun onPlayerEditPlayerMapObject(
            mapObject: PlayerMapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    )

}
