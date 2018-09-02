package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.api.entity.id.TeamId
import ch.leadrian.samp.kamp.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.api.exception.PlayerOfflineException
import java.util.*

interface Player {

    val isOnline: Boolean

    val id: PlayerId

    var locale: Locale

    fun spawn()

    fun setSpawnInfo(spawnInfo: SpawnInfo)

    var coordinates: Vector3D

    var angle: Float

    var interiorId: Int

    var virtualWorldId: Int

    var position: Position

    var location: Location

    var angledLocation: AngledLocation

    var velocity: Vector3D

    fun setCoordinatesFindZ(coordinates: Vector3D)

    fun isStreamedIn(forPlayer: Player): Boolean

    var health: Float

    var armour: Float

    fun setAmmo(weaponModel: WeaponModel, ammo: Int)

    val ammo: Int

    val weaponState: WeaponState

    val targetPlayer: Player?

    val targetActor: Actor?

    var teamId: TeamId

    var score: Int

    var drunkLevel: Int

    var color: Color

    var skin: SkinModel

    var armedWeapon: WeaponModel

    fun getWeaponData(slot: WeaponSlot): WeaponData

    var money: Int

    fun giveMoney(amount: Int)

    fun resetMoney()

    val ipAddress: String

    val gpci: String

    @set:Throws(InvalidPlayerNameException::class)
    var name: String

    val state: PlayerState

    val ping: Int

    val keys: PlayerKeys

    var time: Time

    fun toggleClock(toggle: Boolean)

    fun setWeather(weatherId: Int)

    fun setWeather(weather: Weather)

    fun forceClassSelection()

    var wantedLevel: Int

    var fightingStyle: FightingStyle

    fun playCrimeReport(suspect: Player, crimeReport: CrimeReport)

    fun playAudioStream(url: String, position: Sphere, usePosition: Boolean = true)

    fun playAudioStream(url: String)

    fun stopAudioStream()

    fun setShopName(shopName: ShopName)

    fun setSkillLevel(skill: WeaponSkill, level: Int)

    val surfingVehicle: Vehicle?

    val surfingObject: MapObject?

    fun removeBuilding(modelId: Int, position: Sphere)

    val lastShotVectors: LastShotVectors

    val attachedObjectSlots: List<AttachedObjectSlot>

    val playerVars: PlayerVars

    fun setChatBubble(text: String, color: Color, drawDistance: Float, expireTime: Int)

    val vehicle: Vehicle?

    val vehicleSeat: Int?

    fun removeFromVehicle(): Boolean

    fun toggleControllable(toggle: Boolean)

    fun playSound(soundId: Int, coordinates: Vector3D)

    fun applyAnimation(
            animation: Animation,
            fDelta: Float,
            loop: Boolean,
            lockX: Boolean,
            lockY: Boolean,
            freeze: Boolean,
            time: Int,
            forceSync: Boolean = false
    )

    fun clearAnimation(forceSync: Boolean = false)

    val animationIndex: Int

    var specialAction: SpecialAction

    fun disableRemoteVehicleCollisions(disable: Boolean)

    var checkpoint: Checkpoint?

    var raceCheckpoint: RaceCheckpoint?

    var worldBounds: Rectangle?

    fun showPlayerMarker(player: Player, color: Color)

    fun showPlayerNameTag(player: Player, show: Boolean)

    val mapIcons: List<PlayerMapIcon>

    fun createMapIcon(playerMapIconId: PlayerMapIconId, coordinates: Vector3D, type: MapIconType, color: Color, style: MapIconStyle): PlayerMapIcon

    fun allowTeleport(allow: Boolean)

    var cameraPosition: Vector3D

    fun setCameraLookAt(coordinates: Vector3D, type: CameraType = CameraType.CUT)

    fun setCameraBehind()

    val cameraFrontVector: Vector3D

    val cameraMode: CameraMode

    fun enableCameraTarget(enable: Boolean)

    val cameraTargetObject: MapObject?

    val cameraTargetVehicle: Vehicle?

    val cameraTargetPlayer: Player?

    val cameraTargetActor: Actor?

    val cameraAspectRatio: Float

    val cameraZoom: Float

    fun attachCameraTo(mapObject: MapObject)

    fun attachCameraTo(playerMapObject: PlayerMapObject)

    fun interpolateCameraPosition(from: Vector3D, to: Vector3D, time: Int, type: CameraType = CameraType.CUT)

    fun interpolateCameraLookAt(from: Vector3D, to: Vector3D, time: Int, type: CameraType = CameraType.CUT)

    fun isInVehicle(vehicle: Vehicle): Boolean

    val isInAnyVehicle: Boolean

    fun isInCheckpoint(checkpoint: Checkpoint): Boolean

    val isInAnyCheckpoint: Boolean

    fun isInRaceCheckpoint(raceCheckpoint: RaceCheckpoint): Boolean

    val isInAnyRaceCheckpoint: Boolean

    fun enableStuntBonus(enable: Boolean)

    fun spectate(player: Player, mode: SpectateType = SpectateType.NORMAL)

    fun spectate(vehicle: Vehicle, mode: SpectateType = SpectateType.NORMAL)

    fun stopSpectating()

    val isSpectating: Boolean

    fun startRecording(type: PlayerRecordingType, recordName: String)

    fun stopRecording()

    fun createExplosion(type: ExplosionType, area: Sphere)

    fun createExplosion(type: ExplosionType, coordinates: Vector3D, radius: Float)

    val isAdmin: Boolean

    val isNPC: Boolean

    val isHuman: Boolean

    fun kick()

    fun ban(reason: String? = null)

    val version: String

    val networkStatistics: PlayerNetworkStatistics

    fun selectTextDraw(hoverColor: Color)

    fun cancelSelectTextDraw()

    fun editMapObject(mapObject: MapObject)

    fun editPlayerMapObject(playerMapObject: PlayerMapObject)

    fun selectMapObject()

    fun cancelEditMapObject()

    fun onSpawn(onSpawn: Player.() -> Unit)

    fun onDeath(onDeath: Player.(Player?, WeaponModel) -> Unit)

    fun onDisconnect(onDisconnect: Player.(DisconnectReason) -> Unit)

    val menu: Menu?
}

fun Player.requireOnline(): Player {
    if (!isOnline) throw PlayerOfflineException("Player with ID ${id.value} is already offline")
    return this
}

inline fun <T> Player.requireOnline(block: Player.() -> T): T {
    requireOnline()
    return block(this)
}
