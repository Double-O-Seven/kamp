package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.MapObjectBase
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEditStreamableMapObjectReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerSelectStreamableMapObjectReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectMovedReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectStreamOutReceiver

interface StreamableMapObject : MapObjectBase,
        OnStreamableMapObjectMovedReceiver,
        OnPlayerEditStreamableMapObjectReceiver,
        OnPlayerSelectStreamableMapObjectReceiver,
        OnStreamableMapObjectStreamInReceiver,
        OnStreamableMapObjectStreamOutReceiver {

    val priority: Int

    var interiorIds: MutableSet<Int>

    var virtualWorldIds: MutableSet<Int>

    val isCameraCollisionDisabled: Boolean

    val isAttached: Boolean

    fun refresh()

    fun isStreamedIn(forPlayer: Player): Boolean

    fun setMaterialText(
            textKey: TextKey,
            index: Int = 0,
            size: ObjectMaterialSize = ObjectMaterialSize.SIZE_256X128,
            fontFace: String = "Arial",
            fontSize: Int = 24,
            isBold: Boolean = true,
            fontColor: Color = Colors.WHITE,
            backColor: Color = Colors.TRANSPARENT,
            textAlignment: ObjectMaterialTextAlignment = ObjectMaterialTextAlignment.LEFT
    )

    fun detach()

    fun edit(player: Player)

}