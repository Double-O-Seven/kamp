package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerObjectMovedListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat

class PlayerMapObject
internal constructor(
        override val player: Player,
        override val modelId: Int,
        override val drawDistance: Float,
        coordinates: Vector3D,
        rotation: Vector3D,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<PlayerMapObjectId>, HasPlayer, AbstractDestroyable(), MapObjectBase {

    private val onPlayerObjectMovedListeners = LinkedHashSet<OnPlayerObjectMovedListener>()

    private val onPlayerEditPlayerMapObjectListeners = LinkedHashSet<OnPlayerEditPlayerMapObjectListener>()

    private val onPlayerSelectPlayerMapObjectListeners = LinkedHashSet<OnPlayerSelectPlayerMapObjectListener>()

    override val id: PlayerMapObjectId
        get() = requireNotDestroyed { field }

    init {
        val playerMapObjectId = nativeFunctionExecutor.createPlayerObject(
                playerid = player.id.value,
                modelid = modelId,
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

    fun edit(player: Player) {
        nativeFunctionExecutor.editPlayerObject(playerid = player.id.value, objectid = id.value)
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

    override fun moveTo(coordinates: Vector3D, speed: Float, rotation: Vector3D?): Int {
        return nativeFunctionExecutor.movePlayerObject(
                playerid = player.id.value,
                objectid = id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                Speed = speed,
                RotX = rotation?.x ?: -1000f,
                RotY = rotation?.y ?: -1000f,
                RotZ = rotation?.z ?: -1000f
        )
    }

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

    fun addOnPlayerObjectMovedListener(listener: OnPlayerObjectMovedListener) {
        onPlayerObjectMovedListeners += listener
    }

    fun removeOnPlayerObjectMovedListener(listener: OnPlayerObjectMovedListener) {
        onPlayerObjectMovedListeners -= listener
    }

    inline fun onMoved(crossinline onMoved: PlayerMapObject.() -> Unit): OnPlayerObjectMovedListener {
        val listener = object : OnPlayerObjectMovedListener {

            override fun onPlayerObjectMoved(playerMapObject: PlayerMapObject) {
                onMoved.invoke(playerMapObject)
            }
        }
        addOnPlayerObjectMovedListener(listener)
        return listener
    }

    internal fun onMoved() {
        onPlayerObjectMovedListeners.forEach { it.onPlayerObjectMoved(this) }
    }

    fun addOnPlayerEditPlayerMapObjectListener(listener: OnPlayerEditPlayerMapObjectListener) {
        onPlayerEditPlayerMapObjectListeners += listener
    }

    fun removeOnPlayerEditPlayerMapObjectListener(listener: OnPlayerEditPlayerMapObjectListener) {
        onPlayerEditPlayerMapObjectListeners -= listener
    }

    inline fun onEdit(crossinline onEdit: PlayerMapObject.(ObjectEditResponse, Vector3D, Vector3D) -> Unit): OnPlayerEditPlayerMapObjectListener {
        val listener = object : OnPlayerEditPlayerMapObjectListener {

            override fun onPlayerEditPlayerMapObject(playerMapObject: PlayerMapObject, response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
                onEdit.invoke(this@PlayerMapObject, response, offset, rotation)
            }
        }
        addOnPlayerEditPlayerMapObjectListener(listener)
        return listener
    }

    internal fun onEdit(response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        onPlayerEditPlayerMapObjectListeners.forEach { it.onPlayerEditPlayerMapObject(this, response, offset, rotation) }
    }

    fun addOnPlayerSelectPlayerMapObjectListener(listener: OnPlayerSelectPlayerMapObjectListener) {
        onPlayerSelectPlayerMapObjectListeners += listener
    }

    fun removeOnPlayerSelectPlayerMapObjectListener(listener: OnPlayerSelectPlayerMapObjectListener) {
        onPlayerSelectPlayerMapObjectListeners -= listener
    }

    inline fun onSelect(crossinline onSelect: PlayerMapObject.(Int, Vector3D) -> Unit): OnPlayerSelectPlayerMapObjectListener {
        val listener = object : OnPlayerSelectPlayerMapObjectListener {

            override fun onPlayerSelectPlayerMapObject(playerMapObject: PlayerMapObject, modelId: Int, coordinates: Vector3D) {
                onSelect.invoke(this@PlayerMapObject, modelId, coordinates)
            }
        }
        addOnPlayerSelectPlayerMapObjectListener(listener)
        return listener
    }

    internal fun onSelect(modelId: Int, coordinates: Vector3D) {
        onPlayerSelectPlayerMapObjectListeners.forEach { it.onPlayerSelectPlayerMapObject(this, modelId, coordinates) }
    }

    override var isDestroyed: Boolean = false
        get() = field || !player.isConnected

    override fun onDestroy() {
        nativeFunctionExecutor.destroyPlayerObject(playerid = player.id.value, objectid = id.value)
    }
}