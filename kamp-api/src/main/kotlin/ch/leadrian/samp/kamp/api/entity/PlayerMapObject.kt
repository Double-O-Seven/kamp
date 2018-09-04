package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Colors
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.vector3DOf
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapObjectId

interface PlayerMapObject : HasPlayer, Destroyable, Entity<PlayerMapObjectId> {

    override val id: PlayerMapObjectId

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D)

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D)

    var coordinates: Vector3D

    var rotation: Vector3D

    val model: Int

    fun disableCameraCollision()

    fun moveTo(
            coordinates: Vector3D,
            speed: Float,
            rotation: Vector3D = vector3DOf(x = -1000f, y = -1000f, z = -1000f)
    ): Int

    fun stop()

    val isMoving: Boolean

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color = Colors.TRANSPARENT)

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

    fun onMoved(onMoved: PlayerMapObject.() -> Unit)

    fun onEdit(onEdit: PlayerMapObject.(ObjectEditResponse, Vector3D, Vector3D) -> Boolean)

}