package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId

interface MapObject : Destroyable, Entity<MapObjectId> {

    override val id: MapObjectId

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D)

    fun attachTo(mapObject: MapObject, offset: Vector3D, rotation: Vector3D, syncRotation: Boolean = false)

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D)

    var coordinates: Vector3D

    var rotation: Vector3D

    val drawDistance: Float

    val model: Int

    fun disableCameraCollision()

    fun moveTo(
            coordinates: Vector3D,
            speed: Float,
            rotation: Vector3D = vector3DOf(x = -1000f, y = -1000f, z = -1000f)
    ): Int

    fun stop()

    val isMoving: Boolean

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: ch.leadrian.samp.kamp.core.api.data.Color = Colors.TRANSPARENT)

    fun setMaterialText(
            text: String,
            index: Int = 0,
            size: ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize.SIZE_256X128,
            fontFace: String = "Arial",
            fontSize: Int = 24,
            isBold: Boolean = true,
            fontColor: ch.leadrian.samp.kamp.core.api.data.Color = Colors.WHITE,
            backColor: ch.leadrian.samp.kamp.core.api.data.Color = Colors.TRANSPARENT,
            textAlignment: ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment.LEFT
    )

    fun onMoved(onMoved: MapObject.() -> Unit)

    fun onEdit(onEdit: MapObject.(Player, ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse, Vector3D, Vector3D) -> Unit)
}