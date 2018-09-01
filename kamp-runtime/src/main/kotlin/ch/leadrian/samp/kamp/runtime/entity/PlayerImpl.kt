package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.*
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.api.entity.id.TeamId
import ch.leadrian.samp.kamp.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.factory.PlayerMapIconFactory
import ch.leadrian.samp.kamp.runtime.entity.registry.*
import ch.leadrian.samp.kamp.runtime.types.ReferenceFloat
import ch.leadrian.samp.kamp.runtime.types.ReferenceInt
import ch.leadrian.samp.kamp.runtime.types.ReferenceString
import java.util.*

internal class PlayerImpl(
        id: PlayerId,
        private val actorRegistry: ActorRegistry,
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val mapObjectRegistry: MapObjectRegistry,
        private val menuRegistry: MenuRegistry,
        private val playerMapIconFactory: PlayerMapIconFactory,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : InterceptablePlayer {

    private val onSpawnHandlers: MutableList<Player.() -> Unit> = mutableListOf()

    private val onDeathHandlers: MutableList<Player.(Player?, WeaponModel) -> Unit> = mutableListOf()

    private val onDisconnectHandlers: MutableList<Player.(DisconnectReason) -> Unit> = mutableListOf()

    private val mapIconsById: MutableMap<PlayerMapIconId, PlayerMapIcon> = mutableMapOf()

    override val id: PlayerId = id
        get() = requireOnline { field }

    override var isOnline: Boolean = true
        private set

    override var locale: Locale = Locale.getDefault()

    override fun spawn() {
        nativeFunctionExecutor.spawnPlayer(id.value)
    }

    override fun setSpawnInfo(spawnInfo: SpawnInfo) {
        nativeFunctionExecutor.setSpawnInfo(
                playerid = id.value,
                team = spawnInfo.teamId.value,
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

    override var coordinates: Vector3D
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

    override var angle: Float
        get() {
            val angle = ReferenceFloat()
            nativeFunctionExecutor.getPlayerFacingAngle(playerid = id.value, angle = angle)
            return angle.value
        }
        set(value) {
            nativeFunctionExecutor.setPlayerFacingAngle(playerid = id.value, angle = value)
        }

    override var interiorId: Int
        get() = nativeFunctionExecutor.getPlayerInterior(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerInterior(playerid = id.value, interiorid = value)
        }

    override var virtualWorldId: Int
        get() = nativeFunctionExecutor.getPlayerVirtualWorld(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerVirtualWorld(playerid = id.value, worldid = value)
        }

    override var position: Position
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

    override var location: Location
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

    override var angledLocation: AngledLocation
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

    override fun setCoordinatesFindZ(coordinates: Vector3D) {
        nativeFunctionExecutor.setPlayerPosFindZ(playerid = id.value, x = coordinates.x, y = coordinates.y, z = coordinates.z)
    }

    override fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isPlayerStreamedIn(playerid = id.value, forplayerid = forPlayer.id.value)

    override var health: Float
        get() {
            val health = ReferenceFloat()
            nativeFunctionExecutor.getPlayerHealth(playerid = id.value, health = health)
            return health.value
        }
        set(value) {
            nativeFunctionExecutor.setPlayerHealth(playerid = id.value, health = value)
        }

    override var armour: Float
        get() {
            val armour = ReferenceFloat()
            nativeFunctionExecutor.getPlayerArmour(playerid = id.value, armour = armour)
            return armour.value
        }
        set(value) {
            nativeFunctionExecutor.setPlayerArmour(playerid = id.value, armour = value)
        }

    override fun setAmmo(weaponModel: WeaponModel, ammo: Int) {
        nativeFunctionExecutor.setPlayerAmmo(playerid = id.value, weaponid = weaponModel.value, ammo = ammo)
    }

    override val ammo: Int
        get() = nativeFunctionExecutor.getPlayerAmmo(id.value)

    override val weaponState: WeaponState
        get() = nativeFunctionExecutor.getPlayerWeaponState(id.value).let { WeaponState[it] }

    override val targetPlayer: Player?
        get() = nativeFunctionExecutor.getPlayerTargetPlayer(id.value).let { playerRegistry.getPlayer(it) }

    override val targetActor: Actor?
        get() = nativeFunctionExecutor.getPlayerTargetActor(id.value).let { actorRegistry.getActor(it) }

    override var teamId: TeamId
        get() = nativeFunctionExecutor.getPlayerTeam(id.value).let { TeamId.valueOf(it) }
        set(value) {
            nativeFunctionExecutor.setPlayerTeam(playerid = id.value, teamid = value.value)
        }

    override var score: Int
        get() = nativeFunctionExecutor.getPlayerScore(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerScore(playerid = id.value, score = value)
        }

    override var drunkLevel: Int
        get() = nativeFunctionExecutor.getPlayerDrunkLevel(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerDrunkLevel(playerid = id.value, level = value)
        }

    override var color: Color
        get() = nativeFunctionExecutor.getPlayerColor(id.value).let { colorOf(it) }
        set(value) {
            nativeFunctionExecutor.setPlayerColor(playerid = id.value, color = value.value)
        }

    override var skin: SkinModel
        get() = nativeFunctionExecutor.getPlayerSkin(id.value).let { SkinModel[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerSkin(playerid = id.value, skinid = value.value)
        }

    override var armedWeapon: WeaponModel
        get() = nativeFunctionExecutor.getPlayerWeapon(id.value).let { WeaponModel[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerArmedWeapon(playerid = id.value, weaponid = value.value)
        }

    override fun getWeaponData(slot: WeaponSlot): WeaponData {
        val weapon = ReferenceInt()
        val ammo = ReferenceInt()
        nativeFunctionExecutor.getPlayerWeaponData(playerid = id.value, slot = slot.value, weapon = weapon, ammo = ammo)
        return weaponDataOf(model = WeaponModel[weapon.value], ammo = ammo.value)
    }

    override var money: Int
        get() = nativeFunctionExecutor.getPlayerMoney(id.value)
        set(value) {
            val moneyToGive = value - nativeFunctionExecutor.getPlayerMoney(id.value)
            nativeFunctionExecutor.givePlayerMoney(playerid = id.value, money = moneyToGive)
        }

    override fun giveMoney(amount: Int) {
        nativeFunctionExecutor.givePlayerMoney(playerid = id.value, money = amount)
    }

    override fun resetMoney() {
        nativeFunctionExecutor.resetPlayerMoney(id.value)
    }

    override val ipAddress: String by lazy {
        val ipAddress = ReferenceString()
        nativeFunctionExecutor.getPlayerIp(playerid = id.value, ip = ipAddress, size = 16)
        ipAddress.value ?: "255.255.255.255"
    }

    override val gpci: String by lazy {
        val gpci = ReferenceString()
        nativeFunctionExecutor.gpci(playerid = id.value, buffer = gpci, size = 41)
        gpci.value ?: ""
    }

    override var name: String = ""
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
            val result = nativeFunctionExecutor.setPlayerName(playerid = id.value, name = value)
            when (result) {
                -1 -> throw InvalidPlayerNameException(name = value, message = "Name is already in use, too long or invalid")
                else -> field = value
            }
        }

    override val state: PlayerState
        get() = nativeFunctionExecutor.getPlayerState(id.value).let { PlayerState[it] }

    override val ping: Int
        get() = nativeFunctionExecutor.getPlayerPing(id.value)

    override val keys: PlayerKeys
        get() {
            val keys = ReferenceInt()
            val leftRight = ReferenceInt()
            val upDown = ReferenceInt()
            nativeFunctionExecutor.getPlayerKeys(playerid = id.value, keys = keys, leftright = leftRight, updown = upDown)
            return PlayerKeysImpl(
                    keys = keys.value,
                    leftRight = leftRight.value,
                    upDown = upDown.value,
                    player = this
            )
        }

    override var time: Time
        get() {
            val hour = ReferenceInt()
            val minute = ReferenceInt()
            nativeFunctionExecutor.getPlayerTime(playerid = id.value, hour = hour, minute = minute)
            return timeOf(hour = hour.value, minute = minute.value)
        }
        set(value) {
            nativeFunctionExecutor.setPlayerTime(playerid = id.value, hour = value.hour, minute = value.minute)
        }

    override fun toggleClock(toggle: Boolean) {
        nativeFunctionExecutor.togglePlayerClock(playerid = id.value, toggle = toggle)
    }

    override fun setWeather(weatherId: Int) {
        nativeFunctionExecutor.setPlayerWeather(playerid = id.value, weather = weatherId)
    }

    override fun setWeather(weather: Weather) {
        nativeFunctionExecutor.setPlayerWeather(playerid = id.value, weather = weather.value)
    }

    override fun forceClassSelection() {
        nativeFunctionExecutor.forceClassSelection(id.value)
    }

    override var wantedLevel: Int
        get() = nativeFunctionExecutor.getPlayerWantedLevel(id.value)
        set(value) {
            nativeFunctionExecutor.setPlayerWantedLevel(playerid = id.value, level = value)
        }

    override var fightingStyle: FightingStyle
        get() = nativeFunctionExecutor.getPlayerFightingStyle(id.value).let { FightingStyle[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerFightingStyle(playerid = id.value, style = value.value)
        }

    override fun playCrimeReport(suspect: Player, crimeReport: CrimeReport) {
        nativeFunctionExecutor.playCrimeReportForPlayer(
                playerid = id.value,
                crime = crimeReport.value,
                suspectid = suspect.id.value
        )
    }

    override fun playAudioStream(url: String, position: Sphere, usePosition: Boolean) {
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

    override fun playAudioStream(url: String) {
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

    override fun stopAudioStream() {
        nativeFunctionExecutor.stopAudioStreamForPlayer(id.value)
    }

    override fun setShopName(shopName: ShopName) {
        nativeFunctionExecutor.setPlayerShopName(playerid = id.value, shopname = shopName.value)
    }

    override fun setSkillLevel(skill: WeaponSkill, level: Int) {
        nativeFunctionExecutor.setPlayerSkillLevel(playerid = id.value, skill = skill.value, level = level)
    }

    override val surfingVehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerSurfingVehicleID(id.value).let { vehicleRegistry.getVehicle(it) }

    override val surfingObject: MapObject?
        get() = nativeFunctionExecutor.getPlayerSurfingObjectID(id.value).let { mapObjectRegistry.getMapObject(it) }

    override fun removeBuilding(modelId: Int, position: Sphere) {
        nativeFunctionExecutor.removeBuildingForPlayer(
                playerid = id.value,
                modelid = modelId,
                fX = position.x,
                fY = position.y,
                fZ = position.z,
                fRadius = position.radius
        )
    }

    override val lastShotVectors: LastShotVectors
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

    override val attachedObjectSlots: List<AttachedObjectSlotImpl> =
            (0..9).map {
                AttachedObjectSlotImpl(
                        player = this,
                        index = it,
                        nativeFunctionExecutor = nativeFunctionExecutor
                )
            }.let { Collections.unmodifiableList(it) }

    override val playerVars: PlayerVars = PlayerVarsImpl(this, nativeFunctionExecutor)

    override fun setChatBubble(text: String, color: Color, drawDistance: Float, expireTime: Int) {
        nativeFunctionExecutor.setPlayerChatBubble(
                playerid = id.value,
                text = text,
                color = color.value,
                drawdistance = drawDistance,
                expiretime = expireTime
        )
    }

    override val vehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerVehicleID(id.value).let { vehicleRegistry.getVehicle(it) }

    override val vehicleSeat: Int?
        get() = nativeFunctionExecutor.getPlayerVehicleSeat(id.value).takeIf { it != -1 }

    override fun removeFromVehicle(): Boolean =
            nativeFunctionExecutor.removePlayerFromVehicle(id.value)

    override fun toggleControllable(toggle: Boolean) {
        nativeFunctionExecutor.togglePlayerControllable(playerid = id.value, toggle = toggle)
    }

    override fun playSound(soundId: Int, coordinates: Vector3D) {
        nativeFunctionExecutor.playerPlaySound(
                playerid = id.value,
                soundid = soundId,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z
        )
    }

    override fun applyAnimation(animation: Animation, fDelta: Float, loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean, time: Int, forceSync: Boolean) {
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

    override fun clearAnimation(forceSync: Boolean) {
        nativeFunctionExecutor.clearAnimations(playerid = id.value, forcesync = forceSync)
    }

    override val animationIndex: Int
        get() = nativeFunctionExecutor.getPlayerAnimationIndex(id.value)

    override var specialAction: SpecialAction
        get() = nativeFunctionExecutor.getPlayerSpecialAction(id.value).let { SpecialAction[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerSpecialAction(playerid = id.value, actionid = value.value)
        }

    override fun disableRemoteVehicleCollisions(disable: Boolean) {
        nativeFunctionExecutor.disableRemoteVehicleCollisions(playerid = id.value, disable = disable)
    }

    override var checkpoint: Checkpoint? = null
        set(value) {
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

    override var raceCheckpoint: RaceCheckpoint? = null
        set(value) {
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

    override var worldBounds: Rectangle? = null
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

    override fun showPlayerMarker(player: Player, color: Color) {
        nativeFunctionExecutor.setPlayerMarkerForPlayer(playerid = this.id.value, showplayerid = player.id.value, color = color.value)
    }

    override fun showPlayerNameTag(player: Player, show: Boolean) {
        nativeFunctionExecutor.showPlayerNameTagForPlayer(playerid = this.id.value, showplayerid = player.id.value, show = show)
    }

    override val mapIcons: List<PlayerMapIcon>
        get() = mapIconsById.values.toList()

    override fun createMapIcon(playerMapIconId: PlayerMapIconId, coordinates: Vector3D, type: MapIconType, color: Color, style: MapIconStyle): PlayerMapIcon {
        requireOnline()
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

    internal fun unregisterMapIcon(mapIcon: PlayerMapIcon) {
        mapIconsById.remove(mapIcon.id, mapIcon)
    }

    private fun destroyMapIcons() {
        mapIconsById.values.forEach { it.destroy() }
    }

    override fun allowTeleport(allow: Boolean) {
        nativeFunctionExecutor.allowPlayerTeleport(playerid = this.id.value, allow = allow)
    }

    override var cameraPosition: Vector3D
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

    override fun setCameraLookAt(coordinates: Vector3D, type: CameraType) {
        nativeFunctionExecutor.setPlayerCameraLookAt(
                playerid = id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                cut = type.value
        )
    }

    override fun setCameraBehind() {
        nativeFunctionExecutor.setCameraBehindPlayer(id.value)
    }

    override val cameraFrontVector: Vector3D
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionExecutor.getPlayerCameraFrontVector(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }

    override val cameraMode: CameraMode
        get() = nativeFunctionExecutor.getPlayerCameraMode(id.value).let { CameraMode[it] }

    override fun enableCameraTarget(enable: Boolean) {
        nativeFunctionExecutor.enablePlayerCameraTarget(playerid = id.value, enable = enable)
    }

    override val cameraTargetObject: MapObject?
        get() = nativeFunctionExecutor.getPlayerCameraTargetObject(id.value).let { mapObjectRegistry.getMapObject(it) }

    override val cameraTargetVehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerCameraTargetVehicle(id.value).let { vehicleRegistry.getVehicle(it) }

    override val cameraTargetPlayer: Player?
        get() = nativeFunctionExecutor.getPlayerCameraTargetPlayer(id.value).let { playerRegistry.getPlayer(it) }

    override val cameraTargetActor: Actor?
        get() {
            return nativeFunctionExecutor.getPlayerCameraTargetActor(id.value).let { actorRegistry.getActor(it) }
        }

    override val cameraAspectRatio: Float
        get() = nativeFunctionExecutor.getPlayerCameraAspectRatio(id.value)

    override val cameraZoom: Float
        get() = nativeFunctionExecutor.getPlayerCameraZoom(id.value)

    override fun attachCameraTo(mapObject: MapObject) {
        nativeFunctionExecutor.attachCameraToObject(playerid = id.value, objectid = mapObject.id.value)
    }

    override fun attachCameraTo(playerMapObject: PlayerMapObject) {
        nativeFunctionExecutor.attachCameraToPlayerObject(playerid = id.value, playerobjectid = playerMapObject.id.value)
    }

    override fun interpolateCameraPosition(from: Vector3D, to: Vector3D, time: Int, type: CameraType) {
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

    override fun interpolateCameraLookAt(from: Vector3D, to: Vector3D, time: Int, type: CameraType) {
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

    override fun isInVehicle(vehicle: Vehicle): Boolean =
            nativeFunctionExecutor.isPlayerInVehicle(playerid = id.value, vehicleid = vehicle.id.value)

    override val isInAnyVehicle: Boolean
        get() = nativeFunctionExecutor.isPlayerInAnyVehicle(id.value)

    override fun isInCheckpoint(checkpoint: Checkpoint): Boolean {
        return this.checkpoint == checkpoint && isInAnyCheckpoint
    }

    override val isInAnyCheckpoint: Boolean
        get() = nativeFunctionExecutor.isPlayerInCheckpoint(id.value)

    override fun isInRaceCheckpoint(raceCheckpoint: RaceCheckpoint): Boolean {
        return this.raceCheckpoint == raceCheckpoint && isInAnyRaceCheckpoint
    }

    override val isInAnyRaceCheckpoint: Boolean
        get() {
            return nativeFunctionExecutor.isPlayerInRaceCheckpoint(id.value)
        }

    override fun enableStuntBonus(enable: Boolean) {
        nativeFunctionExecutor.enableStuntBonusForPlayer(playerid = id.value, enable = enable)
    }

    override fun spectate(player: Player, mode: SpectateType) {
        if (!isSpectating) {
            toggleSpectating(true)
        }
        nativeFunctionExecutor.playerSpectatePlayer(
                playerid = this.id.value,
                targetplayerid = player.id.value,
                mode = mode.value
        )
    }

    override fun spectate(vehicle: Vehicle, mode: SpectateType) {
        if (!isSpectating) {
            toggleSpectating(true)
        }
        nativeFunctionExecutor.playerSpectateVehicle(
                playerid = this.id.value,
                targetvehicleid = vehicle.id.value,
                mode = mode.value
        )
    }

    override fun stopSpectating() {
        toggleSpectating(false)
    }

    private fun toggleSpectating(toggle: Boolean) {
        nativeFunctionExecutor.togglePlayerSpectating(playerid = id.value, toggle = toggle)
        isSpectating = toggle
    }

    override var isSpectating: Boolean = false
        private set

    override fun startRecording(type: PlayerRecordingType, recordName: String) {
        nativeFunctionExecutor.startRecordingPlayerData(
                playerid = id.value,
                recordtype = type.value,
                recordname = recordName
        )
    }

    override fun stopRecording() {
        nativeFunctionExecutor.stopRecordingPlayerData(id.value)
    }

    override fun createExplosion(type: ExplosionType, area: Sphere) {
        nativeFunctionExecutor.createExplosionForPlayer(
                playerid = id.value,
                X = area.x,
                Y = area.y,
                Z = area.z,
                type = type.value,
                Radius = area.radius
        )
    }

    override fun createExplosion(type: ExplosionType, coordinates: Vector3D, radius: Float) {
        nativeFunctionExecutor.createExplosionForPlayer(
                playerid = id.value,
                X = coordinates.x,
                Y = coordinates.y,
                Z = coordinates.z,
                type = type.value,
                Radius = radius
        )
    }

    override var isAdmin: Boolean = false
        private set
        get() {
            if (!field) {
                field = nativeFunctionExecutor.isPlayerAdmin(id.value)
            }
            return field
        }

    override val isNPC: Boolean by lazy {
        nativeFunctionExecutor.isPlayerNPC(id.value)
    }

    override val isHuman: Boolean
        get() = !isNPC

    override fun kick() {
        nativeFunctionExecutor.kick(id.value)
    }

    override fun ban(reason: String?) {
        when {
            reason != null && reason.isNotBlank() -> nativeFunctionExecutor.banEx(playerid = id.value, reason = reason)
            else -> nativeFunctionExecutor.ban(id.value)
        }
    }

    override val version: String by lazy {
        val version = ReferenceString()
        nativeFunctionExecutor.getPlayerVersion(playerid = id.value, version = version, len = 24)
        version.value ?: ""
    }

    override val networkStatistics: PlayerNetworkStatistics = PlayerNetworkStatisticsImpl(
            player = this,
            nativeFunctionExecutor = nativeFunctionExecutor
    )

    override fun selectTextDraw(hoverColor: Color) {
        nativeFunctionExecutor.selectTextDraw(playerid = id.value, hovercolor = hoverColor.value)
    }

    override fun cancelSelectTextDraw() {
        nativeFunctionExecutor.cancelSelectTextDraw(id.value)
    }

    override fun editMapObject(mapObject: MapObject) {
        nativeFunctionExecutor.editObject(playerid = id.value, objectid = mapObject.id.value)
    }

    override fun editPlayerMapObject(playerMapObject: PlayerMapObject) {
        nativeFunctionExecutor.editPlayerObject(playerid = id.value, objectid = playerMapObject.id.value)
    }

    override fun selectMapObject() {
        nativeFunctionExecutor.selectObject(id.value)
    }

    override fun cancelEditMapObject() {
        nativeFunctionExecutor.cancelEdit(id.value)
    }

    override val menu: Menu?
        get() = nativeFunctionExecutor.getPlayerMenu(id.value).let { menuRegistry.getMenu(it) }

    override fun onSpawn(onSpawn: Player.() -> Unit) {
        onSpawnHandlers += onSpawn
    }

    override fun onSpawn() {
        onSpawnHandlers.forEach { it.invoke(this) }
    }

    override fun onDeath(onDeath: Player.(Player?, WeaponModel) -> Unit) {
        onDeathHandlers += onDeath
    }

    override fun onDeath(killer: Player?, weapon: WeaponModel) {
        onDeathHandlers.forEach { it.invoke(this, killer, weapon) }
    }

    override fun onDisconnect(onDisconnect: Player.(DisconnectReason) -> Unit) {
        onDisconnectHandlers += onDisconnect
    }

    override fun onDisconnect(reason: DisconnectReason) {
        if (!isOnline) return

        onDisconnectHandlers.forEach { it.invoke(this, reason) }

        isOnline = false

        destroyMapIcons()
    }
}