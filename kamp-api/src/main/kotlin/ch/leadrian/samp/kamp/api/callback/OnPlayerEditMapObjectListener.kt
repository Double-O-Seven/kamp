package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.MapObject
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerEditMapObjectListener {

    fun onPlayerEditMapObject(
            player: Player,
            mapObject: MapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    ): Boolean

}
