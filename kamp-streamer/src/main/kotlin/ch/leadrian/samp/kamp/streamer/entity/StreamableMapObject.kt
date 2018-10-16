package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
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
import ch.leadrian.samp.kamp.core.api.timer.Timer
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject.AttachmentTarget.PlayerAttachmentTarget
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject.AttachmentTarget.VehicleAttachmentTarget
import ch.leadrian.samp.kamp.streamer.util.TimeProvider
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
        private val timerExecutor: TimerExecutor
) : DistanceBasedPlayerStreamable, OnPlayerDisconnectListener {

    private val playerMapObjects: MutableMap<Player, PlayerMapObject> = mutableMapOf()

    private val onMovedHandlers: MutableList<StreamableMapObject.() -> Unit> = mutableListOf()

    private var isCameraCollisionDisabled: Boolean = false

    private var movement: Movement? = null

    private var attachmentTarget: AttachmentTarget? = null

    private val materialsByIndex: MutableMap<Int, Material> = mutableMapOf()

    private val materialTextsByIndex: MutableMap<Int, MaterialText> = mutableMapOf()

    private fun attachTo(attachmentTarget: AttachmentTarget) {
        requireNotDestroyed()
        this.attachmentTarget = attachmentTarget
        playerMapObjects.forEach { _, playerMapObject ->
            attachmentTarget.attach(playerMapObject)
        }
    }

    var coordinates: Vector3D = coordinates
        get() {
            requireNotDestroyed()
            return movement?.coordinates ?: field
        }
        set(value) {
            requireNotDestroyed()
            if (isMoving) {
                stop()
            }
            attachmentTarget = null
            field = value.toVector3D()
            playerMapObjects.forEach { _, playerMapObject ->
                playerMapObject.coordinates = field
            }
        }

    var rotation: Vector3D = rotation
        set(value) {
            requireNotDestroyed()
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

    val isMoving: Boolean
        get() = movement != null

    @JvmOverloads
    fun moveTo(
            coordinates: Vector3D,
            speed: Float,
            rotation: Vector3D = vector3DOf(x = -1000f, y = -1000f, z = -1000f)
    ) {
        requireNotDestroyed()
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
                startTimeInMs = timeProvider.getCurrentTimeInMs()
        )
        movement.timer = timerExecutor.addTimer(movement.duration, TimeUnit.MILLISECONDS) {
            if (this.movement === movement) {
                this.movement = null
                onMovedHandlers.forEach { it.invoke(this) }
            }
        }
        return movement
    }

    fun stop() {
        requireNotDestroyed()
        if (!isMoving) return
        movement?.stopTimer()
        movement = null
        playerMapObjects.forEach { _, playerMapObject ->
            playerMapObject.stop()
        }
        onMovedHandlers.forEach { it.invoke(this) }
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
        val materialText = MaterialText(
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
        materialTextsByIndex[index] = materialText
        playerMapObjects.forEach { _, playerMapObject -> materialText.apply(playerMapObject) }
    }

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
        requireNotDestroyed()
        if (isMoving) {
            stop()
        }
        attachTo(PlayerAttachmentTarget(player = player, offset = offset, rotation = rotation))
    }

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
        requireNotDestroyed()
        if (isMoving) {
            stop()
        }
        attachTo(VehicleAttachmentTarget(vehicle = vehicle, offset = offset, rotation = rotation))
    }

    override fun onStreamIn(forPlayer: Player) {
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
    }

    override fun onStreamOut(forPlayer: Player) {
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

    fun onMoved(onMoved: StreamableMapObject.() -> Unit) {
        onMovedHandlers += onMoved
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        playerMapObjects.remove(player)
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        playerMapObjects.forEach { _, playerMapObject -> playerMapObject.destroy() }
        playerMapObjects.clear()
        isDestroyed = true
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

    private class MaterialText(
            private val text: String,
            private val index: Int,
            private val size: ObjectMaterialSize,
            private val fontFace: String,
            private val fontSize: Int,
            private val isBold: Boolean,
            private val fontColor: Color,
            private val backColor: Color,
            private val textAlignment: ObjectMaterialTextAlignment
    ) {

        fun apply(playerMapObject: PlayerMapObject) {
            playerMapObject.setMaterialText(
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
        }

    }

    private inner class Movement(
            private val origin: Vector3D,
            val destination: Vector3D,
            val rotation: Vector3D,
            val speed: Float,
            private val startTimeInMs: Long
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

        class PlayerAttachmentTarget(
                private val player: Player,
                offset: Vector3D,
                rotation: Vector3D
        ) : AttachmentTarget(offset = offset, rotation = rotation) {

            override fun attach(playerMapObject: PlayerMapObject) {
                playerMapObject.attachTo(player = player, offset = offset, rotation = rotation)
            }

        }

        class VehicleAttachmentTarget(
                private val vehicle: Vehicle,
                offset: Vector3D,
                rotation: Vector3D
        ) : AttachmentTarget(offset = offset, rotation = rotation) {

            override fun attach(playerMapObject: PlayerMapObject) {
                playerMapObject.attachTo(vehicle = vehicle, offset = offset, rotation = rotation)
            }
        }

    }
}