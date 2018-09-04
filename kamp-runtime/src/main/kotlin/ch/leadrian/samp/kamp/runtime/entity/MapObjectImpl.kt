package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.vector3DOf
import ch.leadrian.samp.kamp.api.entity.MapObject
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.runtime.types.ReferenceFloat

internal class MapObjectImpl(
        override val model: Int,
        coordinates: Vector3D,
        rotation: Vector3D,
        override val drawDistance: Float,
        private val mapObjectRegistry: MapObjectRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : MapObject {

    private val onMovedHandlers: MutableList<MapObject.() -> Unit> = mutableListOf()

    private val onEditHandlers: MutableList<MapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit> =
            mutableListOf()

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

    override fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
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

    override fun attachTo(mapObject: MapObject, offset: Vector3D, rotation: Vector3D, syncRotation: Boolean) {
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

    override fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
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

    override var coordinates: Vector3D
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

    override var rotation: Vector3D
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

    override fun disableCameraCollision() {
        nativeFunctionExecutor.setObjectNoCameraCol(id.value)
    }

    override fun moveTo(coordinates: Vector3D, speed: Float, rotation: Vector3D): Int =
            nativeFunctionExecutor.moveObject(
                    objectid = id.value,
                    X = coordinates.x,
                    Y = coordinates.y,
                    Z = coordinates.z,
                    Speed = speed,
                    RotX = rotation.x,
                    RotY = rotation.y,
                    RotZ = rotation.z
            )

    override fun stop() {
        nativeFunctionExecutor.stopObject(id.value)
    }

    override val isMoving: Boolean
        get() = nativeFunctionExecutor.isObjectMoving(id.value)

    override fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color) {
        nativeFunctionExecutor.setObjectMaterial(
                objectid = id.value,
                materialindex = index,
                modelid = modelId,
                materialcolor = color.value,
                texturename = textureName,
                txdname = txdName
        )
    }

    override fun setMaterialText(
            text: String,
            index: Int,
            size: ObjectMaterialSize,
            fontFace: String,
            fontSize: Int,
            isBold: Boolean,
            fontColor: Color,
            backColor: Color,
            textAlignment: ObjectMaterialTextAlignment
    ) {
        nativeFunctionExecutor.setObjectMaterialText(
                objectid = id.value,
                materialindex = index,
                text = text,
                materialsize = size.value,
                fontface = fontFace,
                fontsize = fontSize,
                bold = isBold,
                fontcolor = fontColor.value,
                backcolor = backColor.value,
                textalignment = textAlignment.value
        )
    }

    override fun onMoved(onMoved: MapObject.() -> Unit) {
        onMovedHandlers += onMoved
    }

    internal fun onMoved() {
        onMovedHandlers.forEach { it.invoke(this) }
    }

    override fun onEdit(onEdit: MapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit) {
        onEditHandlers += onEdit
    }

    internal fun onEdit(player: Player, response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        onEditHandlers.forEach { it.invoke(this, player, response, offset, rotation) }
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        nativeFunctionExecutor.destroyObject(id.value)
        mapObjectRegistry.unregister(this)
        isDestroyed = true
    }
}