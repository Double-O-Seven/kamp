package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.TeamId

interface Player {

    val isOnline: Boolean

    val id: PlayerId

    fun spawn()

    fun setSpawnInfo(spawnInfo: SpawnInfo)

    var position3D: Vector3D

    var position: Position

    var location: Location

    var angledLocation: AngledLocation

    var angle: Float

    var interiorId: Int

    var virtualWorld: Int

    fun setPositionFindZ(position: Vector3D)

    fun isStreamedIn(forPlayer: Player): Boolean

    var health: Float

    var armour: Float

    fun setAmmo(weaponModel: WeaponModel, ammo: Int)

    fun getAmmo(weaponModel: WeaponModel): Int

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

    fun resetMoney()

    val ipAddress: String

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

    fun stopAudiStream()

    fun setShopName(shopName: ShopName)

    fun setSkillLevel(skill: WeaponSkill, level: Int)

    val surfingVehicle: Vehicle?

    val surfingObject: MapObject?

    fun removeBuilding(modelId: Int, position: Sphere)

    val lastShotVectors: LastShotVectors

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

    val animation: Animation

    var specialAction: SpecialAction?

    fun disableRemoteVehicleCollisions(disable: Boolean)

    var checkpoint: PlayerCheckpoint?

    var raceCheckpoint: RaceCheckpoint?

    var worldBounds: Rectangle?

    fun showPlayerMarker(player: Player, color: Color)

    fun showPlayerNameTag(player: Player, show: Boolean)

    val mapIcon: PlayerMapIcon?

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

    fun isInVehicle(vehicle: Vehicle)

    val isInAnyVehicle: Boolean

    fun isInCheckpoint(checkpoint: PlayerCheckpoint)

    val isInAnyCheckpoint: Boolean

    fun isInRaceCheckpoint(raceCheckpoint: RaceCheckpoint)

    val isInAnyRaceCheckpoint: Boolean

    var virtualWorldId: Boolean

    fun enableStuntBonus(enable: Boolean)

    fun spectate(player: Player, mode: SpectateType = SpectateType.NORMAL)

    fun spectate(vehicle: Vehicle, mode: SpectateType = SpectateType.NORMAL)

    fun stopSpectating()

    fun createExplosion(type: ExplosionType, area: Sphere)
}