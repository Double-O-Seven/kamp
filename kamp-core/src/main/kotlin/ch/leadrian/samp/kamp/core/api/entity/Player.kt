package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.CameraMode
import ch.leadrian.samp.kamp.core.api.constants.CameraType
import ch.leadrian.samp.kamp.core.api.constants.CrimeReport
import ch.leadrian.samp.kamp.core.api.constants.DefaultPlayerColors
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.ExplosionType
import ch.leadrian.samp.kamp.core.api.constants.FightingStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.constants.PlayerRecordingType
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.ShopName
import ch.leadrian.samp.kamp.core.api.constants.SkinModel
import ch.leadrian.samp.kamp.core.api.constants.SpecialAction
import ch.leadrian.samp.kamp.core.api.constants.SpectateType
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.constants.WeaponSkill
import ch.leadrian.samp.kamp.core.api.constants.WeaponSlot
import ch.leadrian.samp.kamp.core.api.constants.WeaponState
import ch.leadrian.samp.kamp.core.api.constants.Weather
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Animation
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.LastShotVectors
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.SpawnInfo
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Time
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.WeaponData
import ch.leadrian.samp.kamp.core.api.data.angledLocationOf
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.data.timeOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.weaponDataOf
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogNavigation
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import ch.leadrian.samp.kamp.core.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.core.api.exception.PlayerOfflineException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerMapIconFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerMapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextDrawRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextLabelRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import java.util.*

class Player
internal constructor(
        id: PlayerId,
        private val actorRegistry: ActorRegistry,
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val mapObjectRegistry: MapObjectRegistry,
        private val menuRegistry: MenuRegistry,
        private val playerMapIconFactory: PlayerMapIconFactory,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<PlayerId> {

    private val onNameChangeHandlers: MutableList<Player.(String, String) -> Unit> = mutableListOf()

    private val onSpawnHandlers: MutableList<Player.() -> Unit> = mutableListOf()

    private val onDeathHandlers: MutableList<Player.(Player?, WeaponModel) -> Unit> = mutableListOf()

    private val onDisconnectHandlers: MutableList<Player.(DisconnectReason) -> Unit> = mutableListOf()

    private val mapIconsById: MutableMap<PlayerMapIconId, PlayerMapIcon> = mutableMapOf()

    @get:JvmSynthetic
    internal val playerMapObjectRegistry = PlayerMapObjectRegistry()

    @get:JvmSynthetic
    internal val playerTextDrawRegistry = PlayerTextDrawRegistry()

    @get:JvmSynthetic
    internal val playerTextLabelRegistry = PlayerTextLabelRegistry()

    override val id: PlayerId = id
        get() = requireConnected { field }

    var isConnected: Boolean = true
        private set

    var locale: Locale = Locale.getDefault()

    val dialogNavigation: DialogNavigation = DialogNavigation(this)

    fun spawn() {
        nativeFunctionExecutor.spawnPlayer(id.value)
    }

    fun setSpawnInfo(spawnInfo: SpawnInfo) {
        nativeFunctionExecutor.setSpawnInfo(
                playerid = id.value,
                team = spawnInfo.teamId?.value ?: SAMPConstants.NO_TEAM,
                skin = spawnInfo.skinModel.value,
                x = spawnInfo.position.x,
                y = spawnInfo.position.y,
                z = spawnInfo.position.z,
                rotation = spawnInfo.position.angle,
                weapon1 = spawnInfo.weapon1.model.value,
                weapon1_ammo = spawnInfo.weapon1.ammo,
                weapon2 = spawnInfo.weapon2.model.value,
                weapon2_ammo = spawnInfo.weapon2.ammo,
                weapon3 = spawnInfo.weapon3.model.value,
                weapon3_ammo = spawnInfo.weapon3.ammo
        )
    }

    var coordinates: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
        }

    var angle: Float
        get() {
            val angle = ReferenceFloat()
            nativeFunctionExecutor.getPlayerFacingAngle(playerid = id.value, angle = angle)
            return angle.value
        }
        set(value) {
            nativeFunctionExecutor.setPlayerFacingAngle(playerid = id.value, angle = value)
        }

    var interiorId: Int
        get() = nativeFunctionExecutor.getPlayerInterior(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerInterior(playerid = id.value, interiorid = value)
        }

    var virtualWorldId: Int
        get() = nativeFunctionExecutor.getPlayerVirtualWorld(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerVirtualWorld(playerid = id.value, worldid = value)
        }

    var position: Position
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            return positionOf(x = x.value, y = y.value, z = z.value, angle = angle)
        }
        set(value) {
            coordinates = value
            angle = value.angle
        }

    var location: Location
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            return locationOf(
                    x = x.value,
                    y = y.value,
                    z = z.value,
                    interiorId = interiorId,
                    worldId = virtualWorldId
            )
        }
        set(value) {
            coordinates = value
            interiorId = value.interiorId
            virtualWorldId = value.virtualWorldId
        }

    var angledLocation: AngledLocation
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            return angledLocationOf(
                    x = x.value,
                    y = y.value,
                    z = z.value,
                    interiorId = interiorId,
                    worldId = virtualWorldId,
                    angle = angle
            )
        }
        set(value) {
            coordinates = value
            interiorId = value.interiorId
            virtualWorldId = value.virtualWorldId
            angle = value.angle
        }

    var velocity: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerVelocity(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setPlayerVelocity(playerid = id.value, x = value.x, y = value.y, z = value.z)
        }

    fun setCoordinatesFindZ(coordinates: Vector3D) {
        nativeFunctionExecutor.setPlayerPosFindZ(playerid = id.value, x = coordinates.x, y = coordinates.y, z = coordinates.z)
    }

    fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isPlayerStreamedIn(playerid = id.value, forplayerid = forPlayer.id.value)

    var health: Float
        get() {
            val health = ReferenceFloat()
            nativeFunctionExecutor.getPlayerHealth(playerid = id.value, health = health)
            return health.value
        }
        set(value) {
            nativeFunctionExecutor.setPlayerHealth(playerid = id.value, health = value)
        }

    var armour: Float
        get() {
            val armour = ReferenceFloat()
            nativeFunctionExecutor.getPlayerArmour(playerid = id.value, armour = armour)
            return armour.value
        }
        set(value) {
            nativeFunctionExecutor.setPlayerArmour(playerid = id.value, armour = value)
        }

    fun setAmmo(weaponModel: WeaponModel, ammo: Int) {
        nativeFunctionExecutor.setPlayerAmmo(playerid = id.value, weaponid = weaponModel.value, ammo = ammo)
    }

    val ammo: Int
        get() = nativeFunctionExecutor.getPlayerAmmo(id.value)

    val weaponState: WeaponState
        get() = nativeFunctionExecutor.getPlayerWeaponState(id.value).let { WeaponState[it] }

    val targetPlayer: Player?
        get() = nativeFunctionExecutor.getPlayerTargetPlayer(id.value).let { playerRegistry[it] }

    val targetActor: Actor?
        get() = nativeFunctionExecutor.getPlayerTargetActor(id.value).let { actorRegistry[it] }

    var teamId: TeamId
        get() = nativeFunctionExecutor.getPlayerTeam(id.value).let { TeamId.valueOf(it) }
        set(value) {
            nativeFunctionExecutor.setPlayerTeam(playerid = id.value, teamid = value.value)
        }

    var score: Int
        get() = nativeFunctionExecutor.getPlayerScore(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerScore(playerid = id.value, score = value)
        }

    var drunkLevel: Int
        get() = nativeFunctionExecutor.getPlayerDrunkLevel(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerDrunkLevel(playerid = id.value, level = value)
        }

    var color: Color = DefaultPlayerColors[id]
        set(value) {
            nativeFunctionExecutor.setPlayerColor(playerid = id.value, color = value.value)
            field = value.toColor()
        }

    var skin: SkinModel
        get() = nativeFunctionExecutor.getPlayerSkin(id.value).let { SkinModel[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerSkin(playerid = id.value, skinid = value.value)
        }

    var armedWeapon: WeaponModel
        get() = nativeFunctionExecutor.getPlayerWeapon(id.value).let { WeaponModel[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerArmedWeapon(playerid = id.value, weaponid = value.value)
        }

    fun getWeaponData(slot: WeaponSlot): WeaponData {
        val weapon = ReferenceInt()
        val ammo = ReferenceInt()
        nativeFunctionExecutor.getPlayerWeaponData(playerid = id.value, slot = slot.value, weapon = weapon, ammo = ammo)
        return weaponDataOf(model = WeaponModel[weapon.value], ammo = ammo.value)
    }

    var money: Int
        get() = nativeFunctionExecutor.getPlayerMoney(id.value)
        set(value) {
            val moneyToGive = value - nativeFunctionExecutor.getPlayerMoney(id.value)
            nativeFunctionExecutor.givePlayerMoney(playerid = id.value, money = moneyToGive)
        }

    fun giveMoney(amount: Int) {
        nativeFunctionExecutor.givePlayerMoney(playerid = id.value, money = amount)
    }

    fun resetMoney() {
        nativeFunctionExecutor.resetPlayerMoney(id.value)
    }

    val ipAddress: String by lazy {
        val ipAddress = ReferenceString()
        nativeFunctionExecutor.getPlayerIp(playerid = id.value, ip = ipAddress, size = 16)
        ipAddress.value ?: "255.255.255.255"
    }

    val gpci: String by lazy {
        val gpci = ReferenceString()
        nativeFunctionExecutor.gpci(playerid = id.value, buffer = gpci, size = 41)
        gpci.value ?: ""
    }

    var name: String = ""
        get() {
            if (field.isEmpty()) {
                val name = ReferenceString()
                nativeFunctionExecutor.getPlayerName(playerid = id.value, name = name, size = SAMPConstants.MAX_PLAYER_NAME)
                field = name.value.orEmpty()
            }
            return field
        }
        set(value) {
            if (value.isEmpty()) {
                throw InvalidPlayerNameException("", "Name cannot be empty")
            }
            val oldName = name
            val result = nativeFunctionExecutor.setPlayerName(playerid = id.value, name = value)
            when (result) {
                -1 -> throw InvalidPlayerNameException(name = value, message = "Name is already in use, too long or invalid")
                else -> field = value
            }
            onNameChange(oldName, value)
        }

    val state: PlayerState
        get() = nativeFunctionExecutor.getPlayerState(id.value).let { PlayerState[it] }

    val ping: Int
        get() = nativeFunctionExecutor.getPlayerPing(id.value)

    val keys: PlayerKeys
        get() {
            val keys = ReferenceInt()
            val leftRight = ReferenceInt()
            val upDown = ReferenceInt()
            nativeFunctionExecutor.getPlayerKeys(playerid = id.value, keys = keys, leftright = leftRight, updown = upDown)
            return PlayerKeys(
                    keys = keys.value,
                    leftRight = leftRight.value,
                    upDown = upDown.value,
                    player = this
            )
        }

    var time: Time
        get() {
            val hour = ReferenceInt()
            val minute = ReferenceInt()
            nativeFunctionExecutor.getPlayerTime(playerid = id.value, hour = hour, minute = minute)
            return timeOf(hour = hour.value, minute = minute.value)
        }
        set(value) {
            nativeFunctionExecutor.setPlayerTime(playerid = id.value, hour = value.hour, minute = value.minute)
        }

    fun toggleClock(toggle: Boolean) {
        nativeFunctionExecutor.togglePlayerClock(playerid = id.value, toggle = toggle)
    }

    fun setWeather(weatherId: Int) {
        nativeFunctionExecutor.setPlayerWeather(playerid = id.value, weather = weatherId)
    }

    fun setWeather(weather: Weather) {
        nativeFunctionExecutor.setPlayerWeather(playerid = id.value, weather = weather.value)
    }

    fun forceClassSelection() {
        nativeFunctionExecutor.forceClassSelection(id.value)
    }

    var wantedLevel: Int
        get() = nativeFunctionExecutor.getPlayerWantedLevel(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerWantedLevel(playerid = id.value, level = value)
        }

    var fightingStyle: FightingStyle
        get() = nativeFunctionExecutor.getPlayerFightingStyle(id.value).let { FightingStyle[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerFightingStyle(playerid = id.value, style = value.value)
        }

    fun playCrimeReport(suspect: Player, crimeReport: CrimeReport) {
        nativeFunctionExecutor.playCrimeReportForPlayer(
                playerid = id.value,
                crime = crimeReport.value,
                suspectid = suspect.id.value
        )
    }

    @JvmOverloads
    fun playAudioStream(url: String, position: Sphere, usePosition: Boolean = true) {
        nativeFunctionExecutor.playAudioStreamForPlayer(
                playerid = id.value,
                url = url,
                posX = position.x,
                posY = position.y,
                posZ = position.z,
                distance = position.radius,
                usepos = usePosition
        )
    }

    fun playAudioStream(url: String) {
        nativeFunctionExecutor.playAudioStreamForPlayer(
                playerid = id.value,
                url = url,
                posX = 0f,
                posY = 0f,
                posZ = 0f,
                distance = 0f,
                usepos = false
        )
    }

    fun stopAudioStream() {
        nativeFunctionExecutor.stopAudioStreamForPlayer(id.value)
    }

    fun setShopName(shopName: ShopName) {
        nativeFunctionExecutor.setPlayerShopName(playerid = id.value, shopname = shopName.value)
    }

    fun setSkillLevel(skill: WeaponSkill, level: Int) {
        nativeFunctionExecutor.setPlayerSkillLevel(playerid = id.value, skill = skill.value, level = level)
    }

    val surfingVehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerSurfingVehicleID(id.value).let { vehicleRegistry[it] }

    val surfingObject: MapObject?
        get() = nativeFunctionExecutor.getPlayerSurfingObjectID(id.value).let { mapObjectRegistry[it] }

    fun removeBuilding(modelId: Int, position: Sphere) {
        nativeFunctionExecutor.removeBuildingForPlayer(
                playerid = id.value,
                modelid = modelId,
                fX = position.x,
                fY = position.y,
                fZ = position.z,
                fRadius = position.radius
        )
    }

    val lastShotVectors: LastShotVectors
        get() {
            val hitPosX = ReferenceFloat()
            val hitPosY = ReferenceFloat()
            val hitPosZ = ReferenceFloat()
            val originX = ReferenceFloat()
            val originY = ReferenceFloat()
            val originZ = ReferenceFloat()
            nativeFunctionExecutor.getPlayerLastShotVectors(
                    playerid = id.value,
                    fHitPosX = hitPosX,
                    fHitPosY = hitPosY,
                    fHitPosZ = hitPosZ,
                    fOriginX = originX,
                    fOriginY = originY,
                    fOriginZ = originZ
            )
            return LastShotVectors(
                    origin = vector3DOf(x = originX.value, y = originY.value, z = originZ.value),
                    hitPosition = vector3DOf(x = hitPosX.value, y = hitPosY.value, z = hitPosZ.value)
            )
        }

    val attachedObjectSlots: List<AttachedObjectSlot> =
            (0..9).map {
                AttachedObjectSlot(
                        player = this,
                        index = it,
                        nativeFunctionExecutor = nativeFunctionExecutor
                )
            }.let { Collections.unmodifiableList(it) }

    val playerVars: PlayerVars = PlayerVars(this, nativeFunctionExecutor)

    fun setChatBubble(text: String, color: Color, drawDistance: Float, expireTime: Int) {
        nativeFunctionExecutor.setPlayerChatBubble(
                playerid = id.value,
                text = text,
                color = color.value,
                drawdistance = drawDistance,
                expiretime = expireTime
        )
    }

    val vehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerVehicleID(id.value).let { vehicleRegistry[it] }

    val vehicleSeat: Int?
        get() = nativeFunctionExecutor.getPlayerVehicleSeat(id.value).takeIf { it != -1 }

    fun removeFromVehicle(): Boolean =
            nativeFunctionExecutor.removePlayerFromVehicle(id.value)

    fun toggleControllable(toggle: Boolean) {
        nativeFunctionExecutor.togglePlayerControllable(playerid = id.value, toggle = toggle)
    }

    fun playSound(soundId: Int, coordinates: Vector3D) {
        nativeFunctionExecutor.playerPlaySound(
                playerid = id.value,
                soundid = soundId,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z
        )
    }

    @JvmOverloads
    fun applyAnimation(
            animation: Animation,
            fDelta: Float,
            loop: Boolean,
            lockX: Boolean,
            lockY: Boolean,
            freeze: Boolean,
            time: Int,
            forceSync: Boolean = false
    ) {
        nativeFunctionExecutor.applyAnimation(
                playerid = id.value,
                animlib = animation.library,
                animname = animation.animationName,
                fDelta = fDelta,
                loop = loop,
                lockx = lockX,
                locky = lockY,
                freeze = freeze,
                time = time,
                forcesync = forceSync
        )
    }

    @JvmOverloads
    fun clearAnimation(forceSync: Boolean = false) {
        nativeFunctionExecutor.clearAnimations(playerid = id.value, forcesync = forceSync)
    }

    val animationIndex: Int
        get() = nativeFunctionExecutor.getPlayerAnimationIndex(id.value)

    var specialAction: SpecialAction
        get() = nativeFunctionExecutor.getPlayerSpecialAction(id.value).let { SpecialAction[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerSpecialAction(playerid = id.value, actionid = value.value)
        }

    fun disableRemoteVehicleCollisions(disable: Boolean) {
        nativeFunctionExecutor.disableRemoteVehicleCollisions(playerid = id.value, disable = disable)
    }

    var checkpoint: Checkpoint? = null
        set(value) {
            value?.requireNotDestroyed()
            when (value) {
                null -> nativeFunctionExecutor.disablePlayerCheckpoint(id.value)
                else -> nativeFunctionExecutor.setPlayerCheckpoint(
                        playerid = id.value,
                        x = value.coordinates.x,
                        y = value.coordinates.y,
                        z = value.coordinates.z,
                        size = value.size
                )
            }
            field = value
        }

    var raceCheckpoint: RaceCheckpoint? = null
        set(value) {
            value?.requireNotDestroyed()
            when (value) {
                null -> nativeFunctionExecutor.disablePlayerRaceCheckpoint(id.value)
                else -> nativeFunctionExecutor.setPlayerRaceCheckpoint(
                        playerid = id.value,
                        x = value.coordinates.x,
                        y = value.coordinates.y,
                        z = value.coordinates.z,
                        size = value.size,
                        type = value.type.value,
                        nextx = value.nextCoordinates?.x ?: value.coordinates.x,
                        nexty = value.nextCoordinates?.y ?: value.coordinates.y,
                        nextz = value.nextCoordinates?.z ?: value.coordinates.z
                )
            }
            field = value
        }

    var worldBounds: Rectangle? = null
        set(value) {
            nativeFunctionExecutor.setPlayerWorldBounds(
                    playerid = id.value,
                    x_min = value?.minX ?: -20_000f,
                    x_max = value?.maxX ?: 20_000f,
                    y_min = value?.minY ?: -20_000f,
                    y_max = value?.maxY ?: 20_000f
            )
            field = value
        }

    fun showPlayerMarker(player: Player, color: Color) {
        nativeFunctionExecutor.setPlayerMarkerForPlayer(playerid = this.id.value, showplayerid = player.id.value, color = color.value)
    }

    fun showPlayerNameTag(player: Player, show: Boolean) {
        nativeFunctionExecutor.showPlayerNameTagForPlayer(playerid = this.id.value, showplayerid = player.id.value, show = show)
    }

    val mapIcons: List<PlayerMapIcon>
        get() = mapIconsById.values.toList()

    fun createMapIcon(playerMapIconId: PlayerMapIconId, coordinates: Vector3D, type: MapIconType, color: Color, style: MapIconStyle): PlayerMapIcon {
        requireConnected()
        mapIconsById[playerMapIconId]?.destroy()
        val playerMapIcon = playerMapIconFactory.create(
                player = this,
                playerMapIconId = playerMapIconId,
                coordinates = coordinates,
                type = type,
                color = color,
                style = style
        )
        mapIconsById[playerMapIconId] = playerMapIcon
        return playerMapIcon
    }

    @JvmSynthetic
    internal fun unregisterMapIcon(mapIcon: PlayerMapIcon) {
        mapIconsById.remove(mapIcon.id, mapIcon)
    }

    private fun destroyMapIcons() {
        mapIconsById.values.forEach { it.destroy() }
    }

    var cameraPosition: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerCameraPos(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionExecutor.setPlayerCameraPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
        }

    @JvmOverloads
    fun setCameraLookAt(coordinates: Vector3D, type: CameraType = CameraType.CUT) {
        nativeFunctionExecutor.setPlayerCameraLookAt(
                playerid = id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                cut = type.value
        )
    }

    fun setCameraBehind() {
        nativeFunctionExecutor.setCameraBehindPlayer(id.value)
    }

    val cameraFrontVector: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerCameraFrontVector(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }

    val cameraMode: CameraMode
        get() = nativeFunctionExecutor.getPlayerCameraMode(id.value).let { CameraMode[it] }

    fun enableCameraTarget(enable: Boolean) {
        nativeFunctionExecutor.enablePlayerCameraTarget(playerid = id.value, enable = enable)
    }

    val cameraTargetObject: MapObject?
        get() = nativeFunctionExecutor.getPlayerCameraTargetObject(id.value).let { mapObjectRegistry[it] }

    val cameraTargetVehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerCameraTargetVehicle(id.value).let { vehicleRegistry[it] }

    val cameraTargetPlayer: Player?
        get() = nativeFunctionExecutor.getPlayerCameraTargetPlayer(id.value).let { playerRegistry[it] }

    val cameraTargetActor: Actor?
        get() {
            return nativeFunctionExecutor.getPlayerCameraTargetActor(id.value).let { actorRegistry[it] }
        }

    val cameraAspectRatio: Float
        get() = nativeFunctionExecutor.getPlayerCameraAspectRatio(id.value)

    val cameraZoom: Float
        get() = nativeFunctionExecutor.getPlayerCameraZoom(id.value)

    fun attachCameraTo(mapObject: MapObject) {
        nativeFunctionExecutor.attachCameraToObject(playerid = id.value, objectid = mapObject.id.value)
    }

    fun attachCameraTo(playerMapObject: PlayerMapObject) {
        nativeFunctionExecutor.attachCameraToPlayerObject(playerid = id.value, playerobjectid = playerMapObject.id.value)
    }

    @JvmOverloads
    fun interpolateCameraPosition(from: Vector3D, to: Vector3D, time: Int, type: CameraType = CameraType.CUT) {
        nativeFunctionExecutor.interpolateCameraPos(
                playerid = id.value,
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
    fun interpolateCameraLookAt(from: Vector3D, to: Vector3D, time: Int, type: CameraType = CameraType.CUT) {
        nativeFunctionExecutor.interpolateCameraLookAt(
                playerid = id.value,
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

    fun isInVehicle(vehicle: Vehicle): Boolean =
            nativeFunctionExecutor.isPlayerInVehicle(playerid = id.value, vehicleid = vehicle.id.value)

    val isInAnyVehicle: Boolean
        get() = nativeFunctionExecutor.isPlayerInAnyVehicle(id.value)

    fun isInCheckpoint(checkpoint: Checkpoint): Boolean {
        return this.checkpoint === checkpoint && isInAnyCheckpoint
    }

    val isInAnyCheckpoint: Boolean
        get() = nativeFunctionExecutor.isPlayerInCheckpoint(id.value)

    fun isInRaceCheckpoint(raceCheckpoint: RaceCheckpoint): Boolean {
        return this.raceCheckpoint === raceCheckpoint && isInAnyRaceCheckpoint
    }

    val isInAnyRaceCheckpoint: Boolean
        get() {
            return nativeFunctionExecutor.isPlayerInRaceCheckpoint(id.value)
        }

    fun enableStuntBonus() {
        nativeFunctionExecutor.enableStuntBonusForPlayer(id.value, true)
    }

    fun disableStuntBonus() {
        nativeFunctionExecutor.enableStuntBonusForPlayer(id.value, false)
    }

    @JvmOverloads
    fun spectate(player: Player, mode: SpectateType = SpectateType.NORMAL) {
        if (!isSpectating) {
            toggleSpectating(true)
        }
        nativeFunctionExecutor.playerSpectatePlayer(
                playerid = this.id.value,
                targetplayerid = player.id.value,
                mode = mode.value
        )
    }

    @JvmOverloads
    fun spectate(vehicle: Vehicle, mode: SpectateType = SpectateType.NORMAL) {
        if (!isSpectating) {
            toggleSpectating(true)
        }
        nativeFunctionExecutor.playerSpectateVehicle(
                playerid = this.id.value,
                targetvehicleid = vehicle.id.value,
                mode = mode.value
        )
    }

    fun stopSpectating() {
        toggleSpectating(false)
    }

    private fun toggleSpectating(toggle: Boolean) {
        nativeFunctionExecutor.togglePlayerSpectating(playerid = id.value, toggle = toggle)
        isSpectating = toggle
    }

    var isSpectating: Boolean = false
        private set

    fun startRecording(type: PlayerRecordingType, recordName: String) {
        nativeFunctionExecutor.startRecordingPlayerData(
                playerid = id.value,
                recordtype = type.value,
                recordname = recordName
        )
    }

    fun stopRecording() {
        nativeFunctionExecutor.stopRecordingPlayerData(id.value)
    }

    fun createExplosion(type: ExplosionType, area: Sphere) {
        nativeFunctionExecutor.createExplosionForPlayer(
                playerid = id.value,
                X = area.x,
                Y = area.y,
                Z = area.z,
                type = type.value,
                Radius = area.radius
        )
    }

    fun createExplosion(type: ExplosionType, coordinates: Vector3D, radius: Float) {
        nativeFunctionExecutor.createExplosionForPlayer(
                playerid = id.value,
                X = coordinates.x,
                Y = coordinates.y,
                Z = coordinates.z,
                type = type.value,
                Radius = radius
        )
    }

    var isAdmin: Boolean = false
        private set
        get() {
            if (!field) {
                field = nativeFunctionExecutor.isPlayerAdmin(id.value)
            }
            return field
        }

    val isNPC: Boolean by lazy {
        nativeFunctionExecutor.isPlayerNPC(id.value)
    }

    val isHuman: Boolean
        get() = !isNPC

    fun kick() {
        nativeFunctionExecutor.kick(id.value)
    }

    fun ban(reason: String? = null) {
        when {
            reason != null && reason.isNotBlank() -> nativeFunctionExecutor.banEx(playerid = id.value, reason = reason)
            else -> nativeFunctionExecutor.ban(id.value)
        }
    }

    val version: String by lazy {
        val version = ReferenceString()
        nativeFunctionExecutor.getPlayerVersion(playerid = id.value, version = version, len = 24)
        version.value ?: ""
    }

    val networkStatistics: PlayerNetworkStatistics = PlayerNetworkStatistics(
            player = this,
            nativeFunctionExecutor = nativeFunctionExecutor
    )

    fun selectTextDraw(hoverColor: Color) {
        nativeFunctionExecutor.selectTextDraw(playerid = id.value, hovercolor = hoverColor.value)
    }

    fun cancelSelectTextDraw() {
        nativeFunctionExecutor.cancelSelectTextDraw(id.value)
    }

    fun editMapObject(mapObject: MapObject) {
        nativeFunctionExecutor.editObject(playerid = id.value, objectid = mapObject.id.value)
    }

    fun editPlayerMapObject(playerMapObject: PlayerMapObject) {
        nativeFunctionExecutor.editPlayerObject(playerid = id.value, objectid = playerMapObject.id.value)
    }

    fun selectMapObject() {
        nativeFunctionExecutor.selectObject(id.value)
    }

    fun cancelEditMapObject() {
        nativeFunctionExecutor.cancelEdit(id.value)
    }

    val menu: Menu?
        get() = nativeFunctionExecutor.getPlayerMenu(id.value).let { menuRegistry[it] }

    fun sendDeathMessage(victim: Player, weapon: WeaponModel, killer: Player? = null) {
        nativeFunctionExecutor.sendDeathMessageToPlayer(
                playerid = id.value,
                killer = killer?.id?.value ?: SAMPConstants.INVALID_PLAYER_ID,
                killee = victim.id.value,
                weapon = weapon.value
        )
    }

    fun onSpawn(onSpawn: Player.() -> Unit) {
        onSpawnHandlers += onSpawn
    }

    @JvmSynthetic
    internal fun onSpawn() {
        onSpawnHandlers.forEach { it.invoke(this) }
    }

    fun onDeath(onDeath: Player.(Player?, WeaponModel) -> Unit) {
        onDeathHandlers += onDeath
    }

    @JvmSynthetic
    internal fun onDeath(killer: Player?, weapon: WeaponModel) {
        onDeathHandlers.forEach { it.invoke(this, killer, weapon) }
    }

    @JvmSynthetic
    internal fun onDisconnect(onDisconnect: Player.(DisconnectReason) -> Unit) {
        onDisconnectHandlers += onDisconnect
    }

    @JvmSynthetic
    internal fun onNameChange(onNameChange: Player.(String, String) -> Unit) {
        onNameChangeHandlers += onNameChange
    }

    private fun onNameChange(oldName: String, newName: String) {
        onNameChangeHandlers.forEach { it.invoke(this, oldName, newName) }
    }

    @JvmSynthetic
    internal fun onDisconnect(reason: DisconnectReason) {
        if (!isConnected) return

        onDisconnectHandlers.forEach { it.invoke(this, reason) }

        isConnected = false
        destroyMapIcons()
    }

    fun requireConnected(): Player {
        if (!isConnected) throw PlayerOfflineException("Player is already offline")
        return this
    }

    inline fun <T> requireConnected(block: Player.() -> T): T {
        requireConnected()
        return block(this)
    }
}
