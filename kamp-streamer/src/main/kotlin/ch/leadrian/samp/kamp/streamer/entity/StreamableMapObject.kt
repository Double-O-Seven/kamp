package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.timer.Timer
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject.AttachmentTarget.PlayerAttachmentTarget
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject.AttachmentTarget.VehicleAttachmentTarget
import ch.leadrian.samp.kamp.streamer.util.TimeProvider
import com.conversantmedia.util.collection.geometry.Rect3d
import java.util.*
import java.util.concurrent.TimeUnit

class StreamableMapObject
internal constructor(
        val modelId: Int,
        override val priority: Int,
        override val streamDistance: Float,
        coordinates: Vector3D,
        rotation: Vector3D,
        var interiorIds: MutableSet<Int>,
        var virtualWorldIds: MutableSet<Int>,
        private val playerMapObjectService: PlayerMapObjectService,
        private val timeProvider: TimeProvider,
        private val timerExecutor: TimerExecutor,
        private val onStreamableMapObjectMovedHandler: OnStreamableMapObjectMovedHandler,
        private val onPlayerEditStreamableMapObjectHandler: OnPlayerEditStreamableMapObjectHandler,
        private val onPlayerSelectStreamableMapObjectHandler: OnPlayerSelectStreamableMapObjectHandler,
        private val textProvider: TextProvider
) : DistanceBasedPlayerStreamable, SpatiallyIndexedStreamable<StreamableMapObject, Rect3d>(), OnPlayerDisconnectListener {

    private val playerMapObjects: MutableMap<Player, PlayerMapObject> = mutableMapOf()

    private val onStartMovingHandlers: MutableList<StreamableMapObject.() -> Unit> = mutableListOf()

    private val onMovedHandlers: MutableList<StreamableMapObject.() -> Unit> = mutableListOf()

    private val onEditHandlers: MutableList<StreamableMapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit> = mutableListOf()

    private val onSelectHandlers: MutableList<StreamableMapObject.(Player, Int, Vector3D) -> Unit> = mutableListOf()

    private val onAttachHandlers: MutableList<StreamableMapObject.() -> Unit> = mutableListOf()

    private val onDestroyHandlers: MutableList<StreamableMapObject.() -> Unit> = mutableListOf()

    private var isCameraCollisionDisabled: Boolean = false

    private var movement: Movement? = null

    private var attachmentTarget: AttachmentTarget? = null
        get() {
            if (field != null && field?.isValid == false) {
                field = null
            }
            return field
        }

    private val materialsByIndex: MutableMap<Int, Material> = mutableMapOf()

    private val materialTextsByIndex: MutableMap<Int, MaterialText> = mutableMapOf()

    override val self: StreamableMapObject = this

    override var streamInCondition: (Player) -> Boolean = { true }

    var coordinates: Vector3D = coordinates.toVector3D()
        get() = attachmentTarget?.playerMapObjectCoordinates
                ?: movement?.coordinates
                ?: field
        set(value) {
            requireNotDestroyed()
            if (isAttached) return
            cancelMovement()
            field = value.toVector3D()
            playerMapObjects.forEach { _, playerMapObject ->
                playerMapObject.coordinates = field
            }
            onBoundingBoxChanged()
        }

    var rotation: Vector3D = rotation.toVector3D()
        set(value) {
            requireNotDestroyed()
            if (isAttached) return
            field = value.toVector3D()
            playerMapObjects.forEach { _, playerMapObject ->
                playerMapObject.rotation = field
            }
        }

    fun disableCameraCollision() {
        requireNotDestroyed()
        if (isCameraCollisionDisabled) return
        isCameraCollisionDisabled = true
        playerMapObjects.forEach { _, playerMapObject ->
            playerMapObject.disableCameraCollision()
        }
    }

    @JvmOverloads
    fun moveTo(
            coordinates: Vector3D,
            speed: Float,
            rotation: Vector3D = vector3DOf(x = -1000f, y = -1000f, z = -1000f)
    ) {
        requireNotDestroyed()
        if (isAttached) return
        if (!isMoving) {
            onStartMoving()
        }
        this.movement?.stopTimer()
        this.movement = createMovement(coordinates, rotation, speed)
        playerMapObjects.forEach { _, playerMapObject ->
            playerMapObject.moveTo(coordinates = coordinates, speed = speed, rotation = rotation)
        }
    }

    private fun createMovement(destination: Vector3D, rotation: Vector3D, speed: Float): Movement {
        val origin = this.coordinates
        val movement = Movement(
                origin = origin,
                destination = destination.toVector3D(),
                rotation = rotation.toVector3D(),
                speed = speed,
                startTimeInMs = timeProvider.getCurrentTimeInMs(),
                timeProvider = timeProvider
        )
        movement.timer = timerExecutor.addTimer(movement.duration, TimeUnit.MILLISECONDS) {
            if (this.movement === movement) {
                onMoved()
            }
        }
        return movement
    }

    fun stop() {
        requireNotDestroyed()
        if (!isMoving) return
        cancelMovement()
        playerMapObjects.forEach { _, playerMapObject ->
            playerMapObject.stop()
        }
    }

    val isMoving: Boolean
        get() = movement != null

    private fun cancelMovement() {
        movement?.stopTimer()
        movement = null
    }

    private fun onMoved() {
        movement = null
        onMovedHandlers.forEach { it.invoke(this) }
        onStreamableMapObjectMovedHandler.onStreamableMapObjectMoved(this)
    }

    fun onMoved(onMoved: StreamableMapObject.() -> Unit) {
        onMovedHandlers += onMoved
    }

    private fun onStartMoving() {
        onStartMovingHandlers.forEach { it.invoke(this) }
    }

    internal fun onStartMoving(onStartMoving: StreamableMapObject.() -> Unit) {
        onStartMovingHandlers += onStartMoving
    }

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color) {
        requireNotDestroyed()
        val material = Material(index, modelId, txdName, textureName, color)
        materialsByIndex[index] = material
        playerMapObjects.forEach { _, playerMapObject -> material.apply(playerMapObject) }
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
        requireNotDestroyed()
        val materialText = SimpleMaterialText(
                text = text,
                index = index,
                size = size,
                fontFace = fontFace,
                fontSize = fontSize,
                isBold = isBold,
                fontColor = fontColor,
                backColor = backColor,
                textAlignment = textAlignment
        )
        setMaterialText(index, materialText)
    }

    @JvmOverloads
    fun setMaterialText(
            textKey: TextKey,
            index: Int = 0,
            size: ObjectMaterialSize = ObjectMaterialSize.SIZE_256X128,
            fontFace: String = "Arial",
            fontSize: Int = 24,
            isBold: Boolean = true,
            fontColor: Color = Colors.WHITE,
            backColor: Color = Colors.TRANSPARENT,
            textAlignment: ObjectMaterialTextAlignment = ObjectMaterialTextAlignment.LEFT
    ) {
        requireNotDestroyed()
        val materialText = TranslateMaterialText(
                textKey = textKey,
                textProvider = textProvider,
                index = index,
                size = size,
                fontFace = fontFace,
                fontSize = fontSize,
                isBold = isBold,
                fontColor = fontColor,
                backColor = backColor,
                textAlignment = textAlignment
        )
        setMaterialText(index, materialText)
    }

    private fun setMaterialText(index: Int, materialText: MaterialText) {
        materialTextsByIndex[index] = materialText
        playerMapObjects.forEach { _, playerMapObject -> materialText.apply(playerMapObject) }
    }

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
        requireNotDestroyed()
        if (isMoving) {
            cancelMovement()
        }
        attachTo(PlayerAttachmentTarget(player = player, offset = offset, rotation = rotation))
    }

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
        requireNotDestroyed()
        if (isMoving) {
            cancelMovement()
        }
        attachTo(VehicleAttachmentTarget(vehicle = vehicle, offset = offset, rotation = rotation))
    }

    private fun attachTo(attachmentTarget: AttachmentTarget) {
        requireNotDestroyed()
        this.attachmentTarget = attachmentTarget
        playerMapObjects.forEach { _, playerMapObject ->
            attachmentTarget.attach(playerMapObject)
        }
        onAttach()
    }

    fun detach() {
        attachmentTarget?.let { coordinates = it.playerMapObjectCoordinates }
        attachmentTarget = null
        playerMapObjects.forEach { _, playerMapObject -> playerMapObject.destroy() }
        playerMapObjects.clear()
    }

    val isAttached: Boolean
        get() = attachmentTarget != null

    private fun onAttach() {
        onAttachHandlers.forEach { it.invoke(this) }
    }

    internal fun onAttach(onAttach: StreamableMapObject.() -> Unit) {
        onAttachHandlers += onAttach
    }

    override fun onStreamIn(forPlayer: Player) {
        requireNotDestroyed()
        if (playerMapObjects.contains(forPlayer)) {
            throw IllegalStateException("Streamable map object is already streamed in")
        }
        playerMapObjects[forPlayer] = createPlayerMapObject(forPlayer).apply(this::initializePlayerMapObject)
    }

    private fun createPlayerMapObject(forPlayer: Player): PlayerMapObject =
            playerMapObjectService.createPlayerMapObject(
                    player = forPlayer,
                    modelId = modelId,
                    coordinates = coordinates,
                    rotation = rotation,
                    drawDistance = streamDistance
            )

    private fun initializePlayerMapObject(playerMapObject: PlayerMapObject) {
        attachmentTarget?.attach(playerMapObject)
        materialsByIndex.forEach { _, material -> material.apply(playerMapObject) }
        materialTextsByIndex.forEach { _, materialText -> materialText.apply(playerMapObject) }
        movement?.apply(playerMapObject)
        if (isCameraCollisionDisabled) {
            playerMapObject.disableCameraCollision()
        }
        playerMapObject.onEdit { objectEditResponse, offset, rotation ->
            this@StreamableMapObject.onEdit(this.player, objectEditResponse, offset, rotation)
        }
        playerMapObject.onSelect { modelId, offset ->
            this@StreamableMapObject.onSelect(this.player, modelId, offset)
        }
    }

    override fun onStreamOut(forPlayer: Player) {
        requireNotDestroyed()
        val playerMapObject = playerMapObjects.remove(forPlayer)
                ?: throw IllegalStateException("Streamable player map object was not streamed it")
        playerMapObject.destroy()
    }

    override fun isStreamedIn(forPlayer: Player): Boolean = playerMapObjects.contains(forPlayer)

    override fun distanceTo(location: Location): Float =
            when {
                interiorIds.isNotEmpty() && !interiorIds.contains(location.interiorId) -> Float.MAX_VALUE
                virtualWorldIds.isNotEmpty() && !virtualWorldIds.contains(location.virtualWorldId) -> Float.MAX_VALUE
                else -> coordinates.distanceTo(location)
            }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        playerMapObjects.remove(player)
    }

    fun edit(player: Player) {
        playerMapObjects[player]?.edit(player)
    }

    fun onEdit(onEdit: StreamableMapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit) {
        onEditHandlers += onEdit
    }

    private fun onEdit(player: Player, response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        if (response == ObjectEditResponse.FINAL) {
            coordinates = offset
            this.rotation = rotation
        }
        onEditHandlers.forEach { it.invoke(this, player, response, offset, rotation) }
        onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(
                player = player,
                streamableMapObject = this,
                response = response,
                offset = offset,
                rotation = rotation
        )
    }

    fun onSelect(onSelect: StreamableMapObject.(Player, Int, Vector3D) -> Unit) {
        onSelectHandlers += onSelect
    }

    private fun onSelect(player: Player, modelId: Int, offset: Vector3D) {
        onSelectHandlers.forEach { it.invoke(this, player, modelId, offset) }
        onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(player, this, modelId, coordinates)
    }

    fun onDestroy(onDestroy: StreamableMapObject.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        playerMapObjects.forEach { _, playerMapObject -> playerMapObject.destroy() }
        playerMapObjects.clear()
        attachmentTarget = null
        movement = null
        isDestroyed = true
    }

    override fun getBoundingBox(): Rect3d {
        val coordinates = coordinates
        val minX = coordinates.x - streamDistance
        val minY = coordinates.y - streamDistance
        val minZ = coordinates.z - streamDistance
        val maxX = coordinates.x + streamDistance
        val maxY = coordinates.y + streamDistance
        val maxZ = coordinates.z + streamDistance

        return Rect3d(
                minX.toDouble(),
                minY.toDouble(),
                minZ.toDouble(),
                maxX.toDouble(),
                maxY.toDouble(),
                maxZ.toDouble()
        )
    }

    private class Material(
            private val index: Int,
            private val modelId: Int,
            private val txdName: String,
            private val textureName: String,
            private val color: Color
    ) {

        fun apply(playerMapObject: PlayerMapObject) {
            playerMapObject.setMaterial(
                    index = index,
                    modelId = modelId,
                    txdName = txdName,
                    textureName = textureName,
                    color = color
            )
        }

    }

    private abstract class MaterialText(
            private val index: Int,
            private val size: ObjectMaterialSize,
            private val fontFace: String,
            private val fontSize: Int,
            private val isBold: Boolean,
            private val fontColor: Color,
            private val backColor: Color,
            private val textAlignment: ObjectMaterialTextAlignment
    ) {

        protected abstract fun getText(locale: Locale): String

        fun apply(playerMapObject: PlayerMapObject) {
            playerMapObject.setMaterialText(
                    text = getText(playerMapObject.player.locale),
                    index = index,
                    size = size,
                    fontFace = fontFace,
                    fontSize = fontSize,
                    isBold = isBold,
                    fontColor = fontColor,
                    backColor = backColor,
                    textAlignment = textAlignment
            )
        }

    }

    private class SimpleMaterialText(
            private val text: String,
            index: Int,
            size: ObjectMaterialSize,
            fontFace: String,
            fontSize: Int,
            isBold: Boolean,
            fontColor: Color,
            backColor: Color,
            textAlignment: ObjectMaterialTextAlignment
    ) : MaterialText(
            index = index,
            size = size,
            fontFace = fontFace,
            fontSize = fontSize,
            isBold = isBold,
            fontColor = fontColor,
            backColor = backColor,
            textAlignment = textAlignment
    ) {

        override fun getText(locale: Locale): String = text

    }

    private class TranslateMaterialText(
            private val textKey: TextKey,
            private val textProvider: TextProvider,
            index: Int,
            size: ObjectMaterialSize,
            fontFace: String,
            fontSize: Int,
            isBold: Boolean,
            fontColor: Color,
            backColor: Color,
            textAlignment: ObjectMaterialTextAlignment
    ) : MaterialText(
            index = index,
            size = size,
            fontFace = fontFace,
            fontSize = fontSize,
            isBold = isBold,
            fontColor = fontColor,
            backColor = backColor,
            textAlignment = textAlignment
    ) {

        override fun getText(locale: Locale): String = textProvider.getText(locale, textKey)

    }

    private class Movement(
            private val origin: Vector3D,
            val destination: Vector3D,
            val rotation: Vector3D,
            val speed: Float,
            private val startTimeInMs: Long,
            private val timeProvider: TimeProvider
    ) {

        private val distanceToMove = origin.distanceTo(destination)
        val duration: Long = Math.round((distanceToMove / speed) * 1000f).toLong()
        var timer: Timer? = null

        val coordinates: Vector3D
            get() {
                if (duration <= 0) {
                    return destination
                }
                val currentTimeInMs = timeProvider.getCurrentTimeInMs()
                val timeDifference = currentTimeInMs - startTimeInMs
                if (timeDifference >= duration) {
                    return destination
                }
                val progress: Float = timeDifference.toFloat() / duration.toFloat()
                val dx = destination.x - origin.x
                val dy = destination.y - origin.y
                val dz = destination.z - origin.z
                val x = origin.x + progress * dx
                val y = origin.y + progress * dy
                val z = origin.z + progress * dz
                return vector3DOf(x = x, y = y, z = z)
            }

        fun stopTimer() {
            timer?.stop()
            timer = null
        }

        fun apply(playerMapObject: PlayerMapObject) {
            playerMapObject.moveTo(coordinates = destination, speed = speed, rotation = rotation)
        }
    }

    private sealed class AttachmentTarget(val offset: Vector3D, val rotation: Vector3D) {

        abstract fun attach(playerMapObject: PlayerMapObject)

        abstract val coordinates: Vector3D

        abstract val isValid: Boolean

        val playerMapObjectCoordinates: Vector3D
            // TODO include rotation to be really correct
            get() = coordinates + offset

        class PlayerAttachmentTarget(
                private val player: Player,
                offset: Vector3D,
                rotation: Vector3D
        ) : AttachmentTarget(offset = offset, rotation = rotation) {

            override val isValid: Boolean
                get() = player.isConnected

            override fun attach(playerMapObject: PlayerMapObject) {
                playerMapObject.attachTo(player = player, offset = offset, rotation = rotation)
            }

            override val coordinates: Vector3D
                get() = player.coordinates

        }

        class VehicleAttachmentTarget(
                private val vehicle: Vehicle,
                offset: Vector3D,
                rotation: Vector3D
        ) : AttachmentTarget(offset = offset, rotation = rotation) {

            override val isValid: Boolean
                get() = !vehicle.isDestroyed

            override fun attach(playerMapObject: PlayerMapObject) {
                playerMapObject.attachTo(vehicle = vehicle, offset = offset, rotation = rotation)
            }

            override val coordinates: Vector3D
                get() = vehicle.coordinates
        }

    }
}