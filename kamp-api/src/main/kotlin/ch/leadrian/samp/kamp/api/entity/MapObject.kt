package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.id.MapObjectId

interface MapObject : Destroyable {

    val id: MapObjectId

    fun attachTo(player: Player)

    fun attachTo(mapObject: MapObject)

    fun attachTo(vehicle: Vehicle)

    var coordinates: Vector3D

    var rotation: Vector3D

    val model: Int

    fun disableCameraCollision()

    fun moveTo(coordinates: Vector3D, speed: Float, rotation: Vector3D? = null)

    fun stop()

    val isMoving: Boolean

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color? = null)

    fun setMaterialText(
            text: String,
            index: Int = 0,
            size: ObjectMaterialSize = ObjectMaterialSize.SIZE_256X128,
            fontFace: String = "Arial",
            fontSize: Int = 24,
            isBold: Boolean = true,
            fontColor: Color? = null,
            backColor: Color? = null,
            textAlignment: ObjectMaterialTextAlignment = ObjectMaterialTextAlignment.LEFT
    )

    fun onMoved(onMoved: MapObject.() -> Unit)

    fun onEdit(onEdit: MapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Boolean)
}