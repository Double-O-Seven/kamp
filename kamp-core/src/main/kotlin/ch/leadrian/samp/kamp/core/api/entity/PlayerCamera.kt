package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.CameraMode
import ch.leadrian.samp.kamp.core.api.constants.CameraType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat

class PlayerCamera
internal constructor(
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val mapObjectRegistry: MapObjectRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val playerRegistry: PlayerRegistry,
        private val actorRegistry: ActorRegistry
) : HasPlayer {

    var coordinates: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerCameraPos(playerid = player.id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setPlayerCameraPos(playerid = player.id.value, x = value.x, y = value.y, z = value.z)
        }

    val mode: CameraMode
        get() = nativeFunctionExecutor.getPlayerCameraMode(player.id.value).let { CameraMode[it] }

    var isTargetEnabled: Boolean = false
        set(value) {
            field = value
            nativeFunctionExecutor.enablePlayerCameraTarget(playerid = player.id.value, enable = value)
        }

    val targetMapObject: MapObject?
        get() = nativeFunctionExecutor.getPlayerCameraTargetObject(player.id.value).let { mapObjectRegistry[it] }

    val targetVehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerCameraTargetVehicle(player.id.value).let { vehicleRegistry[it] }

    val targetPlayer: Player?
        get() = nativeFunctionExecutor.getPlayerCameraTargetPlayer(player.id.value).let { playerRegistry[it] }

    val targetActor: Actor?
        get() = nativeFunctionExecutor.getPlayerCameraTargetActor(player.id.value).let { actorRegistry[it] }

    val aspectRatio: Float
        get() = nativeFunctionExecutor.getPlayerCameraAspectRatio(player.id.value)

    val zoom: Float
        get() = nativeFunctionExecutor.getPlayerCameraZoom(player.id.value)

    val frontVector: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerCameraFrontVector(playerid = player.id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }

    @JvmOverloads
    fun lookAt(coordinates: Vector3D, type: CameraType = CameraType.CUT) {
        nativeFunctionExecutor.setPlayerCameraLookAt(
                playerid = player.id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                cut = type.value
        )
    }

    fun setBehind() {
        nativeFunctionExecutor.setCameraBehindPlayer(player.id.value)
    }

    fun attachTo(mapObject: MapObject) {
        nativeFunctionExecutor.attachCameraToObject(playerid = player.id.value, objectid = mapObject.id.value)
    }

    fun attachTo(playerMapObject: PlayerMapObject) {
        nativeFunctionExecutor.attachCameraToPlayerObject(
                playerid = player.id.value,
                playerobjectid = playerMapObject.id.value
        )
    }

    @JvmOverloads
    fun interpolateCoordinates(from: Vector3D, to: Vector3D, time: Int, type: CameraType = CameraType.CUT) {
        nativeFunctionExecutor.interpolateCameraPos(
                playerid = player.id.value,
                FromX = from.x,
                FromY = from.y,
                FromZ = from.z,
                ToX = to.x,
                ToY = to.y,
                ToZ = to.z,
                time = time,
                cut = type.value
        )
    }

    @JvmOverloads
    fun interpolateLookAt(from: Vector3D, to: Vector3D, time: Int, type: CameraType = CameraType.CUT) {
        nativeFunctionExecutor.interpolateCameraLookAt(
                playerid = player.id.value,
                FromX = from.x,
                FromY = from.y,
                FromZ = from.z,
                ToX = to.x,
                ToY = to.y,
                ToZ = to.z,
                time = time,
                cut = type.value
        )
    }

}