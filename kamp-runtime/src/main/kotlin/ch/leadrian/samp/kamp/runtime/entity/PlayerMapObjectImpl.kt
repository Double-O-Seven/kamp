package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.data.vector3DOf
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.PlayerMapObjectRegistry
import ch.leadrian.samp.kamp.runtime.types.ReferenceFloat

internal class PlayerMapObjectImpl(
        override val player: Player,
        override val model: Int,
        override val drawDistance: Float,
        coordinates: Vector3D,
        rotation: Vector3D,
        private val playerMapObjectRegistry: PlayerMapObjectRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : PlayerMapObject {

    private val onMovedHandlers: MutableList<PlayerMapObject.() -> Unit> = mutableListOf()

    private val onEditHandlers: MutableList<PlayerMapObject.(ObjectEditResponse, Vector3D, Vector3D) -> Unit> =
            mutableListOf()

    override val id: PlayerMapObjectId
        get() = requireNotDestroyed { field }

    init {
        val playerMapObjectId = nativeFunctionExecutor.createPlayerObject(
                playerid = player.id.value,
                modelid = model,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                rX = rotation.x,
                rY = rotation.y,
                rZ = rotation.z,
                DrawDistance = drawDistance
        )

        if (playerMapObjectId == SAMPConstants.INVALID_OBJECT_ID) {
            throw CreationFailedException("Could not create map object")
        }

        id = PlayerMapObjectId.valueOf(playerMapObjectId)
    }

    override fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
        nativeFunctionExecutor.attachPlayerObjectToPlayer(
                objectid = id.value,
                objectplayer = this.player.id.value,
                attachplayer = player.id.value,
                OffsetX = offset.x,
                OffsetY = offset.y,
                OffsetZ = offset.z,
                rX = rotation.x,
                rY = rotation.y,
                rZ = rotation.z
        )
    }

    override fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
        nativeFunctionExecutor.attachPlayerObjectToVehicle(
                playerid = player.id.value,
                objectid = id.value,
                vehicleid = vehicle.id.value,
                fOffsetX = offset.x,
                fOffsetY = offset.y,
                fOffsetZ = offset.z,
                fRotX = rotation.x,
                fRotY = rotation.y,
                RotZ = rotation.z
        )
    }

    override var coordinates: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerObjectPos(
                    playerid = player.id.value,
                    objectid = id.value,
                    x = x,
                    y = y,
                    z = z
            )
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setPlayerObjectPos(
                    playerid = player.id.value,
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
            nativeFunctionExecutor.getPlayerObjectRot(
                    playerid = player.id.value,
                    objectid = id.value,
                    rotX = x,
                    rotY = y,
                    rotZ = z
            )
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setPlayerObjectRot(
                    playerid = player.id.value,
                    objectid = id.value,
                    rotX = value.x,
                    rotY = value.y,
                    rotZ = value.z
            )
        }

    override fun disableCameraCollision() {
        nativeFunctionExecutor.setPlayerObjectNoCameraCol(playerid = player.id.value, objectid = id.value)
    }

    override fun moveTo(coordinates: Vector3D, speed: Float, rotation: Vector3D): Int =
            nativeFunctionExecutor.movePlayerObject(
                    playerid = player.id.value,
                    objectid = id.value,
                    x = coordinates.x,
                    y = coordinates.y,
                    z = coordinates.z,
                    Speed = speed,
                    RotX = rotation.x,
                    RotY = rotation.y,
                    RotZ = rotation.z
            )

    override fun stop() {
        nativeFunctionExecutor.stopPlayerObject(playerid = player.id.value, objectid = id.value)
    }

    override val isMoving: Boolean
        get() = nativeFunctionExecutor.isPlayerObjectMoving(playerid = player.id.value, objectid = id.value)

    override fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color) {
        nativeFunctionExecutor.setPlayerObjectMaterial(
                playerid = player.id.value,
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
        nativeFunctionExecutor.setPlayerObjectMaterialText(
                playerid = player.id.value,
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

    override fun onMoved(onMoved: PlayerMapObject.() -> Unit) {
        onMovedHandlers += onMoved
    }

    internal fun onMoved() {
        onMovedHandlers.forEach { it.invoke(this) }
    }

    override fun onEdit(onEdit: PlayerMapObject.(ObjectEditResponse, Vector3D, Vector3D) -> Unit) {
        onEditHandlers += onEdit
    }

    internal fun onEdit(response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        onEditHandlers.forEach { it.invoke(this, response, offset, rotation) }
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        nativeFunctionExecutor.destroyPlayerObject(playerid = player.id.value, objectid = id.value)
        playerMapObjectRegistry.unregister(this)
        isDestroyed = true
    }
}