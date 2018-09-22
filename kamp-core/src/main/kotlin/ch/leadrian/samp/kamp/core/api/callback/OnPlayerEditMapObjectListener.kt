package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerEditMapObjectListener {

    fun onPlayerEditMapObject(
            player: Player,
            mapObject: MapObject,
            response: ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    )

}
