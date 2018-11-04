package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject

interface OnPlayerEditStreamableMapObjectListener {

    fun onPlayerEditStreamableMapObject(
            player: Player,
            streamableMapObject: StreamableMapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    )

}
