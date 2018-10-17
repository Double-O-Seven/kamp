package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat

class MapObject
internal constructor(
        val model: Int,
        coordinates: Vector3D,
        rotation: Vector3D,
        val drawDistance: Float,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<MapObjectId>, AbstractDestroyable() {

    private val onMovedHandlers: MutableList<MapObject.() -> Unit> = mutableListOf()

    private val onEditHandlers: MutableList<MapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit> =
            mutableListOf()

    private val onDestroyHandlers: MutableList<MapObject.() -> Unit> = mutableListOf()

    override val id: MapObjectId
        get() = requireNotDestroyed { field }

    init {
        val mapObjectId = nativeFunctionExecutor.createObject(
                modelid = model,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                DrawDistance = drawDistance,
                rX = rotation.x,
                rY = rotation.y,
                rZ = rotation.z
        )

        if (mapObjectId == SAMPConstants.INVALID_OBJECT_ID) {
            throw CreationFailedException("Could not create map object")
        }

        id = MapObjectId.valueOf(mapObjectId)
    }

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
        nativeFunctionExecutor.attachObjectToPlayer(
                objectid = id.value,
                playerid = player.id.value,
                fOffsetX = offset.x,
                fOffsetY = offset.y,
                fOffsetZ = offset.z,
                fRotX = rotation.x,
                fRotY = rotation.y,
                fRotZ = rotation.z
        )
    }

    @JvmOverloads
    fun attachTo(mapObject: MapObject, offset: Vector3D, rotation: Vector3D, syncRotation: Boolean = false) {
        nativeFunctionExecutor.attachObjectToObject(
                objectid = id.value,
                attachtoid = mapObject.id.value,
                fOffsetX = offset.x,
                fOffsetY = offset.y,
                fOffsetZ = offset.z,
                fRotX = rotation.x,
                fRotY = rotation.y,
                fRotZ = rotation.z,
                SyncRotation = syncRotation
        )
    }

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
        nativeFunctionExecutor.attachObjectToVehicle(
                objectid = id.value,
                vehicleid = vehicle.id.value,
                fOffsetX = offset.x,
                fOffsetY = offset.y,
                fOffsetZ = offset.z,
                fRotX = rotation.x,
                fRotY = rotation.y,
                fRotZ = rotation.z
        )
    }

    var coordinates: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getObjectPos(
                    objectid = id.value,
                    x = x,
                    y = y,
                    z = z
            )
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setObjectPos(
                    objectid = id.value,
                    x = value.x,
                    y = value.y,
                    z = value.z
            )
        }

    var rotation: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getObjectRot(
                    objectid = id.value,
                    rotX = x,
                    rotY = y,
                    rotZ = z
            )
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setObjectRot(
                    objectid = id.value,
                    rotX = value.x,
                    rotY = value.y,
                    rotZ = value.z
            )
        }

    fun disableCameraCollision() {
        nativeFunctionExecutor.setObjectNoCameraCol(id.value)
    }

    @JvmOverloads
    fun moveTo(
            coordinates: Vector3D,
            speed: Float,
            rotation: Vector3D = vector3DOf(x = -1000f, y = -1000f, z = -1000f)
    ): Int = nativeFunctionExecutor.moveObject(
            objectid = id.value,
            X = coordinates.x,
            Y = coordinates.y,
            Z = coordinates.z,
            Speed = speed,
            RotX = rotation.x,
            RotY = rotation.y,
            RotZ = rotation.z
    )

    fun stop() {
        nativeFunctionExecutor.stopObject(id.value)
    }

    val isMoving: Boolean
        get() = nativeFunctionExecutor.isObjectMoving(id.value)

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color) {
        nativeFunctionExecutor.setObjectMaterial(
                objectid = id.value,
                materialindex = index,
                modelid = modelId,
                materialcolor = color.argb,
                texturename = textureName,
                txdname = txdName
        )
    }

    @JvmOverloads
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
    ) {
        nativeFunctionExecutor.setObjectMaterialText(
                objectid = id.value,
                materialindex = index,
                text = text,
                materialsize = size.value,
                fontface = fontFace,
                fontsize = fontSize,
                bold = isBold,
                fontcolor = fontColor.argb,
                backcolor = backColor.argb,
                textalignment = textAlignment.value
        )
    }

    fun onMoved(onMoved: MapObject.() -> Unit) {
        onMovedHandlers += onMoved
    }

    @JvmSynthetic
    internal fun onMoved() {
        onMovedHandlers.forEach { it.invoke(this) }
    }

    fun onEdit(onEdit: MapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit) {
        onEditHandlers += onEdit
    }

    @JvmSynthetic
    internal fun onEdit(player: Player, response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        onEditHandlers.forEach { it.invoke(this, player, response, offset, rotation) }
    }

    fun onDestroy(onDestroy: MapObject.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.destroyObject(id.value)
        isDestroyed = true
    }
}