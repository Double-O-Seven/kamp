package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnMapObjectMovedReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditMapObjectReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectMapObjectReceiver
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnMapObjectMovedReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerEditMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerSelectMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat

class MapObject
internal constructor(
        override val modelId: Int,
        coordinates: Vector3D,
        rotation: Vector3D,
        override val drawDistance: Float,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onMapObjectMovedReceiver: OnMapObjectMovedReceiverDelegate = OnMapObjectMovedReceiverDelegate(),
        private val onPlayerEditMapObjectReceiver: OnPlayerEditMapObjectReceiverDelegate = OnPlayerEditMapObjectReceiverDelegate(),
        private val onPlayerSelectMapObjectReceiver: OnPlayerSelectMapObjectReceiverDelegate = OnPlayerSelectMapObjectReceiverDelegate()
) : Entity<MapObjectId>,
        AbstractDestroyable(),
        MapObjectBase,
        OnMapObjectMovedReceiver by onMapObjectMovedReceiver,
        OnPlayerEditMapObjectReceiver by onPlayerEditMapObjectReceiver,
        OnPlayerSelectMapObjectReceiver by onPlayerSelectMapObjectReceiver {

    override val id: MapObjectId
        get() = requireNotDestroyed { field }

    init {
        val mapObjectId = nativeFunctionExecutor.createObject(
                modelid = modelId,
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

    fun edit(player: Player) {
        nativeFunctionExecutor.editObject(playerid = player.id.value, objectid = id.value)
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

    override fun moveTo(
            coordinates: Vector3D,
            speed: Float,
            rotation: Vector3D?
    ): Int = nativeFunctionExecutor.moveObject(
            objectid = id.value,
            X = coordinates.x,
            Y = coordinates.y,
            Z = coordinates.z,
            Speed = speed,
            RotX = rotation?.x ?: -1000f,
            RotY = rotation?.y ?: -1000f,
            RotZ = rotation?.z ?: -1000f
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
                materialcolor = color.argb,
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
                fontcolor = fontColor.argb,
                backcolor = backColor.argb,
                textalignment = textAlignment.value
        )
    }

    internal fun onMoved() {
        onMapObjectMovedReceiver.onMapObjectMoved(this)
    }

    internal fun onEdit(player: Player, response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        onPlayerEditMapObjectReceiver.onPlayerEditMapObject(player, this, response, offset, rotation)
    }

    internal fun onSelect(player: Player, modelId: Int, coordinates: Vector3D) {
        onPlayerSelectMapObjectReceiver.onPlayerSelectMapObject(player, this, modelId, coordinates)
    }

    override fun onDestroy() {
        nativeFunctionExecutor.destroyObject(id.value)
    }
}