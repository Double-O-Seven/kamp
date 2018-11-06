package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface StreamableMapObject : Destroyable {

    val modelId: Int

    val priority: Int

    val streamDistance: Float

    var interiorIds: MutableSet<Int>

    var virtualWorldIds: MutableSet<Int>

    var coordinates: Vector3D

    var rotation: Vector3D

    val isCameraCollisionDisabled: Boolean

    val isMoving: Boolean

    val isAttached: Boolean
    
    fun refresh()

    fun isStreamedIn(forPlayer: Player): Boolean

    fun onStreamIn(onStreamIn: StreamableMapObject.(Player) -> Unit)

    fun onStreamOut(onStreamOut: StreamableMapObject.(Player) -> Unit)

    fun disableCameraCollision()

    fun moveTo(destination: Vector3D, speed: Float, targetRotation: Vector3D? = null)

    fun stop()

    fun onMoved(onMoved: StreamableMapObject.() -> Unit)

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color)

    fun setMaterialText(
            text: String,
            index: Int = 0,
            size: ObjectMaterialSize = ObjectMaterialSize.SIZE_256X128,
            fontFace: String = "Arial",
            fontSize: Int = 24,
            isBold: Boolean = true,
            fontColor: Color = Colors.WHITE,
            backColor: Color = Colors.TRANSPARENT,
            textAlignment: ObjectMaterialTextAlignment = ObjectMaterialTextAlignment.LEFT
    )

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

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D)

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D)

    fun detach()

    fun edit(player: Player)

    fun onEdit(onEdit: StreamableMapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit)

    fun onSelect(onSelect: StreamableMapObject.(Player, Int, Vector3D) -> Unit)
}