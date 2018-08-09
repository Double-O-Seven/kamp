package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.TeamId
import ch.leadrian.samp.kamp.api.exception.InvalidPlayerNameException
import java.util.*

interface Player {

    val isOnline: Boolean

    val id: PlayerId

    var locale: Locale

    fun spawn()

    fun setSpawnInfo(spawnInfo: SpawnInfo)

    var coordinates: Vector3D

    var position: Position

    var location: Location

    var angledLocation: AngledLocation

    var angle: Float

    var interiorId: Int

    var virtualWorldId: Int

    fun setPositionFindZ(coordinates: Vector3D)

    fun isStreamedIn(forPlayer: Player): Boolean

    var health: Float

    var armour: Float

    fun setAmmo(weaponModel: WeaponModel, ammo: Int)

    val ammo: Int

    val weaponState: WeaponState

    val targetPlayer: Player?

    val targetActor: Actor?

    var team: TeamId

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

    @set:Throws(InvalidPlayerNameException::class)
    var name: String

    val state: PlayerState

    val ping: Int

    val keys: PlayerKeys

    var time: Time

    fun toggleClock(toggle: Boolean)

    fun setWeather(weatherId: Int)

    fun setWeather(weather: Weather) {
        setWeather(weather.value)
    }

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

    fun getAttachedObjectSlot(index: Int): AttachedObjectSlot

    val playerVars: PlayerVars

    fun setChatBubble(text: String, color: Color, drawDistance: Float, expireTime: Int)

    val vehicle: Vehicle?

    val vehicleSeat: Int?

    fun removeFromVehicle()

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

    fun addMapIcon(mapIcon: PlayerMapIcon, index: Int)

    fun removeMapIcon(mapIcon: PlayerMapIcon)

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

    fun attachCameraToObject(mapObject: MapObject)

    fun attachCameraToPlayerObject(playerMapObject: PlayerMapObject)

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

    fun cancelSelectMapObject()

    fun onSpawn(onSpawn: Player.() -> Boolean)

    fun onDeath(onDeath: Player.(Player?, WeaponModel) -> Boolean)

    val menu: Menu?
}