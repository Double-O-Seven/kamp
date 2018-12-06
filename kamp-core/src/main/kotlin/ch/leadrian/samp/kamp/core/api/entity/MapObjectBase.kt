package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D

interface MapObjectBase : Destroyable {

    val modelId: Int

    val drawDistance: Float

    var coordinates: Vector3D

    var rotation: Vector3D

    val isMoving: Boolean

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D)

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D)

    fun disableCameraCollision()

    fun moveTo(coordinates: Vector3D, speed: Float, rotation: Vector3D? = null): Int

    fun stop()

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
}