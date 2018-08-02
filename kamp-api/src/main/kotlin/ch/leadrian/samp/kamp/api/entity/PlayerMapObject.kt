package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.colorOf
import ch.leadrian.samp.kamp.api.data.vector3DOf
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapObjectId

interface PlayerMapObject : HasPlayer, Destroyable {

    val id: PlayerMapObjectId

    fun attachTo(player: Player)

    fun attachTo(vehicle: Vehicle)

    var coordinates: Vector3D

    var rotation: Vector3D

    val model: Int

    fun disableCameraCollision()

    fun moveTo(coordinates: Vector3D, speed: Float, rotation: Vector3D = vector3DOf(x = -1000f, y = -1000f, z = -1000f))

    fun stop()

    val isMoving: Boolean

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color = colorOf(0))

    fun setMaterialText(
            text: String,
            index: Int = 0,
            size: ObjectMaterialSize = ObjectMaterialSize.SIZE_256X128,
            fontface: String = "Arial",
            fontSize: Int = 24,
            isBold: Boolean = true,
            fontColor: Color = colorOf(0xFFFFFFFF),
            backColor: Color = colorOf(0),
            textAlignment: ObjectMaterialTextAlignment = ObjectMaterialTextAlignment.LEFT
    )

}