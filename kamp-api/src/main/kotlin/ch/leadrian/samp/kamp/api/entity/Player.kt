package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.TeamId

interface Player {

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

    fun setPositionFindZ(position: Position)

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

    // TODO fun playAudioStream(url: URL, position: Sphere)

    // TODO fun playAudioStream(url: URL)

    fun stopAudiStream()

    fun setShopName(shopName: ShopName)

    fun setSkillLevel(skill: WeaponSkill, level: Int)

}