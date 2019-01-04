package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerEditPlayerMapObjectListener {

    @InlineCallback("onEdit")
    fun onPlayerEditPlayerMapObject(
            @Receiver playerMapObject: PlayerMapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    )

}
