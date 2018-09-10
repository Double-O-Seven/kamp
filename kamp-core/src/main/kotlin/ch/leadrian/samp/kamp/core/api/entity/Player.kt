package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.*
import ch.leadrian.samp.kamp.core.api.data.LastShotVectors
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.SpawnInfo
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Time
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.WeaponData
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import ch.leadrian.samp.kamp.core.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.core.api.exception.PlayerOfflineException
import java.util.*

interface Player : Entity<PlayerId> {

    val isConnected: Boolean

    override val id: PlayerId

    var locale: Locale

    fun spawn()

    fun setSpawnInfo(spawnInfo: SpawnInfo)

    var coordinates: Vector3D

    var angle: Float

    var interiorId: Int

    var virtualWorldId: Int

    var position: Position

    var location: Location

    var angledLocation: ch.leadrian.samp.kamp.core.api.data.AngledLocation

    var velocity: Vector3D

    fun setCoordinatesFindZ(coordinates: Vector3D)

    fun isStreamedIn(forPlayer: Player): Boolean

    var health: Float

    var armour: Float

    fun setAmmo(weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, ammo: Int)

    val ammo: Int

    val weaponState: ch.leadrian.samp.kamp.core.api.constants.WeaponState

    val targetPlayer: Player?

    val targetActor: Actor?

    var teamId: TeamId

    var score: Int

    var drunkLevel: Int

    var color: ch.leadrian.samp.kamp.core.api.data.Color

    var skin: ch.leadrian.samp.kamp.core.api.constants.SkinModel

    var armedWeapon: ch.leadrian.samp.kamp.core.api.constants.WeaponModel

    fun getWeaponData(slot: ch.leadrian.samp.kamp.core.api.constants.WeaponSlot): WeaponData

    var money: Int

    fun giveMoney(amount: Int)

    fun resetMoney()

    val ipAddress: String

    val gpci: String

    @set:Throws(InvalidPlayerNameException::class)
    var name: String

    val state: ch.leadrian.samp.kamp.core.api.constants.PlayerState

    val ping: Int

    val keys: PlayerKeys

    var time: Time

    fun toggleClock(toggle: Boolean)

    fun setWeather(weatherId: Int)

    fun setWeather(weather: ch.leadrian.samp.kamp.core.api.constants.Weather)

    fun forceClassSelection()

    var wantedLevel: Int

    var fightingStyle: ch.leadrian.samp.kamp.core.api.constants.FightingStyle

    fun playCrimeReport(suspect: Player, crimeReport: ch.leadrian.samp.kamp.core.api.constants.CrimeReport)

    fun playAudioStream(url: String, position: Sphere, usePosition: Boolean = true)

    fun playAudioStream(url: String)

    fun stopAudioStream()

    fun setShopName(shopName: ch.leadrian.samp.kamp.core.api.constants.ShopName)

    fun setSkillLevel(skill: ch.leadrian.samp.kamp.core.api.constants.WeaponSkill, level: Int)

    val surfingVehicle: Vehicle?

    val surfingObject: MapObject?

    fun removeBuilding(modelId: Int, position: Sphere)

    val lastShotVectors: LastShotVectors

    val attachedObjectSlots: List<AttachedObjectSlot>

    val playerVars: PlayerVars

    fun setChatBubble(text: String, color: ch.leadrian.samp.kamp.core.api.data.Color, drawDistance: Float, expireTime: Int)

    val vehicle: Vehicle?

    val vehicleSeat: Int?

    fun removeFromVehicle(): Boolean

    fun toggleControllable(toggle: Boolean)

    fun playSound(soundId: Int, coordinates: Vector3D)

    fun applyAnimation(
            animation: ch.leadrian.samp.kamp.core.api.data.Animation,
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

    var specialAction: ch.leadrian.samp.kamp.core.api.constants.SpecialAction

    fun disableRemoteVehicleCollisions(disable: Boolean)

    var checkpoint: Checkpoint?

    var raceCheckpoint: RaceCheckpoint?

    var worldBounds: Rectangle?

    fun showPlayerMarker(player: Player, color: ch.leadrian.samp.kamp.core.api.data.Color)

    fun showPlayerNameTag(player: Player, show: Boolean)

    val mapIcons: List<PlayerMapIcon>

    fun createMapIcon(playerMapIconId: PlayerMapIconId, coordinates: Vector3D, type: ch.leadrian.samp.kamp.core.api.constants.MapIconType, color: ch.leadrian.samp.kamp.core.api.data.Color, style: ch.leadrian.samp.kamp.core.api.constants.MapIconStyle): PlayerMapIcon

    fun allowTeleport(allow: Boolean)

    var cameraPosition: Vector3D

    fun setCameraLookAt(coordinates: Vector3D, type: ch.leadrian.samp.kamp.core.api.constants.CameraType = ch.leadrian.samp.kamp.core.api.constants.CameraType.CUT)

    fun setCameraBehind()

    val cameraFrontVector: Vector3D

    val cameraMode: ch.leadrian.samp.kamp.core.api.constants.CameraMode

    fun enableCameraTarget(enable: Boolean)

    val cameraTargetObject: MapObject?

    val cameraTargetVehicle: Vehicle?

    val cameraTargetPlayer: Player?

    val cameraTargetActor: Actor?

    val cameraAspectRatio: Float

    val cameraZoom: Float

    fun attachCameraTo(mapObject: MapObject)

    fun attachCameraTo(playerMapObject: PlayerMapObject)

    fun interpolateCameraPosition(from: Vector3D, to: Vector3D, time: Int, type: ch.leadrian.samp.kamp.core.api.constants.CameraType = ch.leadrian.samp.kamp.core.api.constants.CameraType.CUT)

    fun interpolateCameraLookAt(from: Vector3D, to: Vector3D, time: Int, type: ch.leadrian.samp.kamp.core.api.constants.CameraType = ch.leadrian.samp.kamp.core.api.constants.CameraType.CUT)

    fun isInVehicle(vehicle: Vehicle): Boolean

    val isInAnyVehicle: Boolean

    fun isInCheckpoint(checkpoint: Checkpoint): Boolean

    val isInAnyCheckpoint: Boolean

    fun isInRaceCheckpoint(raceCheckpoint: RaceCheckpoint): Boolean

    val isInAnyRaceCheckpoint: Boolean

    fun enableStuntBonus(enable: Boolean)

    fun spectate(player: Player, mode: ch.leadrian.samp.kamp.core.api.constants.SpectateType = ch.leadrian.samp.kamp.core.api.constants.SpectateType.NORMAL)

    fun spectate(vehicle: Vehicle, mode: ch.leadrian.samp.kamp.core.api.constants.SpectateType = ch.leadrian.samp.kamp.core.api.constants.SpectateType.NORMAL)

    fun stopSpectating()

    val isSpectating: Boolean

    fun startRecording(type: ch.leadrian.samp.kamp.core.api.constants.PlayerRecordingType, recordName: String)

    fun stopRecording()

    fun createExplosion(type: ch.leadrian.samp.kamp.core.api.constants.ExplosionType, area: Sphere)

    fun createExplosion(type: ch.leadrian.samp.kamp.core.api.constants.ExplosionType, coordinates: Vector3D, radius: Float)

    val isAdmin: Boolean

    val isNPC: Boolean

    val isHuman: Boolean

    fun kick()

    fun ban(reason: String? = null)

    val version: String

    val networkStatistics: PlayerNetworkStatistics

    fun selectTextDraw(hoverColor: ch.leadrian.samp.kamp.core.api.data.Color)

    fun cancelSelectTextDraw()

    fun editMapObject(mapObject: MapObject)

    fun editPlayerMapObject(playerMapObject: PlayerMapObject)

    fun selectMapObject()

    fun cancelEditMapObject()

    fun onSpawn(onSpawn: Player.() -> Unit)

    fun onDeath(onDeath: Player.(Player?, ch.leadrian.samp.kamp.core.api.constants.WeaponModel) -> Unit)

    val menu: Menu?
}

fun Player.requireOnline(): Player {
    if (!isConnected) throw PlayerOfflineException("Player is already offline")
    return this
}

inline fun <T> Player.requireOnline(block: Player.() -> T): T {
    requireOnline()
    return block(this)
}
