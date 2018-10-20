package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat

class PlayerMapObject
internal constructor(
        override val player: Player,
        val model: Int,
        val drawDistance: Float,
        coordinates: Vector3D,
        rotation: Vector3D,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<PlayerMapObjectId>, HasPlayer, AbstractDestroyable() {

    private val onMovedHandlers: MutableList<PlayerMapObject.() -> Unit> = mutableListOf()

    private val onEditHandlers: MutableList<PlayerMapObject.(ObjectEditResponse, Vector3D, Vector3D) -> Unit> = mutableListOf()

    private val onSelectHandlers: MutableList<PlayerMapObject.(Int, Vector3D) -> Unit> = mutableListOf()

    private val onDestroyHandlers: MutableList<PlayerMapObject.() -> Unit> = mutableListOf()

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

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
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

    fun edit(player: Player) {
        nativeFunctionExecutor.editPlayerObject(playerid = player.id.value, objectid = id.value)
    }

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
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

    var coordinates: Vector3D
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

    var rotation: Vector3D
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

    fun disableCameraCollision() {
        nativeFunctionExecutor.setPlayerObjectNoCameraCol(playerid = player.id.value, objectid = id.value)
    }

    @JvmOverloads
    fun moveTo(
            coordinates: Vector3D,
            speed: Float,
            rotation: Vector3D = vector3DOf(x = -1000f, y = -1000f, z = -1000f)
    ): Int = nativeFunctionExecutor.movePlayerObject(
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

    fun stop() {
        nativeFunctionExecutor.stopPlayerObject(playerid = player.id.value, objectid = id.value)
    }

    val isMoving: Boolean
        get() = nativeFunctionExecutor.isPlayerObjectMoving(playerid = player.id.value, objectid = id.value)

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color) {
        nativeFunctionExecutor.setPlayerObjectMaterial(
                playerid = player.id.value,
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
        nativeFunctionExecutor.setPlayerObjectMaterialText(
                playerid = player.id.value,
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

    fun onMoved(onMoved: PlayerMapObject.() -> Unit) {
        onMovedHandlers += onMoved
    }

    internal fun onMoved() {
        onMovedHandlers.forEach { it.invoke(this) }
    }

    fun onEdit(onEdit: PlayerMapObject.(ObjectEditResponse, Vector3D, Vector3D) -> Unit) {
        onEditHandlers += onEdit
    }

    internal fun onEdit(response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        onEditHandlers.forEach { it.invoke(this, response, offset, rotation) }
    }

    fun onSelect(onSelect: PlayerMapObject.(Int, Vector3D) -> Unit) {
        onSelectHandlers += onSelect
    }

    internal fun onSelect(modelId: Int, offset: Vector3D) {
        onSelectHandlers.forEach { it.invoke(this, modelId, offset) }
    }

    fun onDestroy(onDestroy: PlayerMapObject.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set
        get() = field || !player.isConnected

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.destroyPlayerObject(playerid = player.id.value, objectid = id.value)
        isDestroyed = true
    }
}