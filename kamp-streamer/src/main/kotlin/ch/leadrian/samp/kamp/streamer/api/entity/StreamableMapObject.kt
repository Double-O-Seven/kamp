package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.MapObjectBase
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface StreamableMapObject : MapObjectBase {

    val priority: Int

    var interiorIds: MutableSet<Int>

    var virtualWorldIds: MutableSet<Int>

    val isCameraCollisionDisabled: Boolean

    val isAttached: Boolean

    fun refresh()

    fun isStreamedIn(forPlayer: Player): Boolean

    fun onStreamIn(onStreamIn: StreamableMapObject.(Player) -> Unit)

    fun onStreamOut(onStreamOut: StreamableMapObject.(Player) -> Unit)

    override fun disableCameraCollision()

    override fun stop()

    fun onMoved(onMoved: StreamableMapObject.() -> Unit)

    override fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color)

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

    fun onEdit(onEdit: StreamableMapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit)

    fun onSelect(onSelect: StreamableMapObject.(Player, Int, Vector3D) -> Unit)
}