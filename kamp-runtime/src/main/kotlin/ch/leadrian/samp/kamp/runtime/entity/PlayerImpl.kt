package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.*
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.api.entity.id.TeamId
import ch.leadrian.samp.kamp.api.exception.InvalidPlayerNameException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.runtime.entity.registry.VehicleRegistry
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
        private val nativeFunctionsExecutor: SAMPNativeFunctionExecutor
) : Player {

    override var id: PlayerId = id
        private set

    override var isOnline: Boolean = true
        private set

    override var locale: Locale = Locale.getDefault()

    internal fun onDisconnect() {
        if (!isOnline) return
        isOnline = false

        destroyMapIcons()

        playerRegistry.unregister(this)
        this.id = PlayerId.INVALID
    }

    private fun destroyMapIcons() {
        mapIconsById.values.forEach { it.destroy() }
    }

    override fun spawn() {
        requireOnline()
        nativeFunctionsExecutor.spawnPlayer(id.value)
    }

    override fun setSpawnInfo(spawnInfo: SpawnInfo) {
        requireOnline()
        nativeFunctionsExecutor.setSpawnInfo(
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
            requireOnline()
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
        }

    override var position: Position
        get() {
            requireOnline()
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            val angle = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            nativeFunctionsExecutor.getPlayerFacingAngle(playerid = id.value, angle = angle)
            return positionOf(x = x.value, y = y.value, z = z.value, angle = angle.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
            nativeFunctionsExecutor.setPlayerFacingAngle(playerid = id.value, angle = value.angle)
        }

    override var location: Location
        get() {
            requireOnline()
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            val interiorId = nativeFunctionsExecutor.getPlayerInterior(id.value)
            val virtualWorldId = nativeFunctionsExecutor.getPlayerVirtualWorld(id.value)
            nativeFunctionsExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            return locationOf(
                    x = x.value,
                    y = y.value,
                    z = z.value,
                    interiorId = interiorId,
                    worldId = virtualWorldId
            )
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
            nativeFunctionsExecutor.setPlayerInterior(playerid = id.value, interiorid = value.interiorId)
            nativeFunctionsExecutor.setPlayerVirtualWorld(playerid = id.value, worldid = value.virtualWorldId)
        }

    override var angledLocation: AngledLocation
        get() {
            requireOnline()
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            val interiorId = nativeFunctionsExecutor.getPlayerInterior(id.value)
            val virtualWorldId = nativeFunctionsExecutor.getPlayerVirtualWorld(id.value)
            val angle = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            nativeFunctionsExecutor.getPlayerFacingAngle(playerid = id.value, angle = angle)
            return angledLocationOf(
                    x = x.value,
                    y = y.value,
                    z = z.value,
                    interiorId = interiorId,
                    worldId = virtualWorldId,
                    angle = angle.value
            )
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
            nativeFunctionsExecutor.setPlayerInterior(playerid = id.value, interiorid = value.interiorId)
            nativeFunctionsExecutor.setPlayerVirtualWorld(playerid = id.value, worldid = value.virtualWorldId)
            nativeFunctionsExecutor.setPlayerFacingAngle(playerid = id.value, angle = value.angle)
        }

    override var angle: Float
        get() {
            requireOnline()
            val angle = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerFacingAngle(playerid = id.value, angle = angle)
            return angle.value
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerFacingAngle(playerid = id.value, angle = value)
        }

    override var interiorId: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerInterior(id.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerInterior(playerid = id.value, interiorid = value)
        }

    override var virtualWorldId: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerVirtualWorld(id.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerVirtualWorld(playerid = id.value, worldid = value)
        }

    override fun setPositionFindZ(coordinates: Vector3D) {
        requireOnline()
        nativeFunctionsExecutor.setPlayerPosFindZ(playerid = id.value, x = position.x, y = position.y, z = position.z)
    }

    override fun isStreamedIn(forPlayer: Player): Boolean {
        requireOnline()
        forPlayer.requireOnline()
        return nativeFunctionsExecutor.isPlayerStreamedIn(playerid = id.value, forplayerid = forPlayer.id.value)
    }

    override var health: Float
        get() {
            requireOnline()
            val health = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerHealth(playerid = id.value, health = health)
            return health.value
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerHealth(playerid = id.value, health = value)
        }

    override var armour: Float
        get() {
            requireOnline()
            val armour = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerArmour(playerid = id.value, armour = armour)
            return armour.value
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerArmour(playerid = id.value, armour = value)
        }

    override fun setAmmo(weaponModel: WeaponModel, ammo: Int) {
        requireOnline()
        nativeFunctionsExecutor.setPlayerAmmo(playerid = id.value, weaponid = weaponModel.value, ammo = ammo)
    }

    override val ammo: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerAmmo(id.value)
        }

    override val weaponState: WeaponState
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerWeaponState(id.value).let { WeaponState[it] }
        }

    override val targetPlayer: Player?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerTargetPlayer(id.value).let { playerRegistry.getPlayer(it) }
        }

    override val targetActor: Actor?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerTargetActor(id.value).let { actorRegistry.getActor(it) }
        }

    override var team: TeamId
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerTeam(id.value).let { TeamId.valueOf(it) }
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerTeam(playerid = id.value, teamid = value.value)
        }

    override var score: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerScore(id.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerScore(playerid = id.value, score = value)
        }

    override var drunkLevel: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerDrunkLevel(id.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerDrunkLevel(playerid = id.value, level = value)
        }

    override var color: Color
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerColor(id.value).let { colorOf(it) }
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerColor(playerid = id.value, color = value.value)
        }

    override var skin: SkinModel
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerSkin(id.value).let { SkinModel[it] }
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerSkin(playerid = id.value, skinid = value.value)
        }

    override var armedWeapon: WeaponModel
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerWeapon(id.value).let { WeaponModel[it] }
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerArmedWeapon(playerid = id.value, weaponid = value.value)
        }

    override fun getWeaponData(slot: WeaponSlot): WeaponData {
        requireOnline()
        val weapon = ReferenceInt()
        val ammo = ReferenceInt()
        nativeFunctionsExecutor.getPlayerWeaponData(playerid = id.value, slot = slot.value, weapon = weapon, ammo = ammo)
        return weaponDataOf(model = WeaponModel[weapon.value], ammo = ammo.value)
    }

    override var money: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerMoney(id.value)
        }
        set(value) {
            requireOnline()
            val moneyToGive = value - nativeFunctionsExecutor.getPlayerMoney(id.value)
            nativeFunctionsExecutor.givePlayerMoney(playerid = id.value, money = moneyToGive)
        }

    override fun giveMoney(amount: Int) {
        requireOnline()
        nativeFunctionsExecutor.givePlayerMoney(playerid = id.value, money = amount)
    }

    override fun resetMoney() {
        requireOnline()
        nativeFunctionsExecutor.resetPlayerMoney(id.value)
    }

    override val ipAddress: String by lazy {
        requireOnline()
        val ipAddress = ReferenceString()
        nativeFunctionsExecutor.getPlayerIp(playerid = id.value, ip = ipAddress, size = 16)
        ipAddress.value ?: "255.255.255.255"
    }

    override val gpci: String by lazy {
        requireOnline()
        val gpci = ReferenceString()
        nativeFunctionsExecutor.gpci(playerid = id.value, buffer = gpci, size = 41)
        gpci.value ?: ""
    }

    override var name: String = ""
        get() {
            requireOnline()
            if (field.isEmpty()) {
                val name = ReferenceString()
                nativeFunctionsExecutor.getPlayerName(playerid = id.value, name = name, size = SAMPConstants.MAX_PLAYER_NAME)
                field = name.value.orEmpty()
            }
            return field
        }
        set(value) {
            requireOnline()
            if (value.isEmpty()) {
                throw InvalidPlayerNameException("", "Name cannot be empty")
            }
            val result = nativeFunctionsExecutor.setPlayerName(playerid = id.value, name = value)
            when (result) {
                -1 -> throw InvalidPlayerNameException(name = value, message = "Name is already in use, too long or invalid")
                else -> field = value
            }
        }

    override val state: PlayerState
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerState(id.value).let { PlayerState[it] }
        }

    override val ping: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerPing(id.value)
        }

    override val keys: PlayerKeys
        get() {
            requireOnline()
            val keys = ReferenceInt()
            val leftRight = ReferenceInt()
            val upDown = ReferenceInt()
            nativeFunctionsExecutor.getPlayerKeys(playerid = id.value, keys = keys, leftright = leftRight, updown = upDown)
            return PlayerKeysImpl(
                    keys = keys.value,
                    leftRight = leftRight.value,
                    upDown = upDown.value,
                    player = this
            )
        }

    override var time: Time
        get() {
            requireOnline()
            val hour = ReferenceInt()
            val minute = ReferenceInt()
            nativeFunctionsExecutor.getPlayerTime(playerid = id.value, hour = hour, minute = minute)
            return timeOf(hour = hour.value, minute = minute.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerTime(playerid = id.value, hour = value.hour, minute = value.minute)
        }

    override fun toggleClock(toggle: Boolean) {
        requireOnline()
        nativeFunctionsExecutor.togglePlayerClock(playerid = id.value, toggle = toggle)
    }

    override fun setWeather(weatherId: Int) {
        requireOnline()
        nativeFunctionsExecutor.setPlayerWeather(playerid = id.value, weather = weatherId)
    }

    override fun setWeather(weather: Weather) {
        requireOnline()
        nativeFunctionsExecutor.setPlayerWeather(playerid = id.value, weather = weather.value)
    }

    override fun forceClassSelection() {
        requireOnline()
        nativeFunctionsExecutor.forceClassSelection(id.value)
    }

    override var wantedLevel: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerWantedLevel(id.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerWantedLevel(playerid = id.value, level = value)
        }

    override var fightingStyle: FightingStyle
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerFightingStyle(id.value).let { FightingStyle[it] }
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerFightingStyle(playerid = id.value, style = value.value)
        }

    override fun playCrimeReport(suspect: Player, crimeReport: CrimeReport) {
        requireOnline()
        suspect.requireOnline()
        nativeFunctionsExecutor.playCrimeReportForPlayer(
                playerid = id.value,
                crime = crimeReport.value,
                suspectid = suspect.id.value
        )
    }

    override fun playAudioStream(url: String, position: Sphere, usePosition: Boolean) {
        requireOnline()
        nativeFunctionsExecutor.playAudioStreamForPlayer(
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
        requireOnline()
        nativeFunctionsExecutor.playAudioStreamForPlayer(
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
        requireOnline()
        nativeFunctionsExecutor.stopAudioStreamForPlayer(id.value)
    }

    override fun setShopName(shopName: ShopName) {
        requireOnline()
        nativeFunctionsExecutor.setPlayerShopName(playerid = id.value, shopname = shopName.value)
    }

    override fun setSkillLevel(skill: WeaponSkill, level: Int) {
        requireOnline()
        nativeFunctionsExecutor.setPlayerSkillLevel(playerid = id.value, skill = skill.value, level = level)
    }

    override val surfingVehicle: Vehicle?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerSurfingVehicleID(id.value).let { vehicleRegistry.getVehicle(it) }
        }

    override val surfingObject: MapObject?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerSurfingObjectID(id.value).let { mapObjectRegistry.getMapObject(it) }
        }

    override fun removeBuilding(modelId: Int, position: Sphere) {
        requireOnline()
        nativeFunctionsExecutor.removeBuildingForPlayer(
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
            requireOnline()
            val hitPosX = ReferenceFloat()
            val hitPosY = ReferenceFloat()
            val hitPosZ = ReferenceFloat()
            val originX = ReferenceFloat()
            val originY = ReferenceFloat()
            val originZ = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerLastShotVectors(
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
                        nativeFunctionsExecutor = nativeFunctionsExecutor
                )
            }.let { Collections.unmodifiableList(it) }

    override val playerVars: PlayerVars = PlayerVarsImpl(this, nativeFunctionsExecutor)

    override fun setChatBubble(text: String, color: Color, drawDistance: Float, expireTime: Int) {
        requireOnline()
        nativeFunctionsExecutor.setPlayerChatBubble(
                playerid = id.value,
                text = text,
                color = color.value,
                drawdistance = drawDistance,
                expiretime = expireTime
        )
    }

    override val vehicle: Vehicle?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerVehicleID(id.value).let { vehicleRegistry.getVehicle(it) }
        }

    override val vehicleSeat: Int?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerVehicleSeat(id.value).takeIf { it != -1 }
        }

    override fun removeFromVehicle() {
        requireOnline()
        nativeFunctionsExecutor.removePlayerFromVehicle(id.value)
    }

    override fun toggleControllable(toggle: Boolean) {
        requireOnline()
        nativeFunctionsExecutor.togglePlayerControllable(playerid = id.value, toggle = toggle)
    }

    override fun playSound(soundId: Int, coordinates: Vector3D) {
        requireOnline()
        nativeFunctionsExecutor.playerPlaySound(
                playerid = id.value,
                soundid = soundId,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z
        )
    }

    override fun applyAnimation(animation: Animation, fDelta: Float, loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean, time: Int, forceSync: Boolean) {
        requireOnline()
        nativeFunctionsExecutor.applyAnimation(
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
        requireOnline()
        nativeFunctionsExecutor.clearAnimations(playerid = id.value, forcesync = forceSync)
    }

    override val animationIndex: Int
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerAnimationIndex(id.value)
        }

    override var specialAction: SpecialAction
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerSpecialAction(id.value).let { SpecialAction[it] }
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerSpecialAction(playerid = id.value, actionid = value.value)
        }

    override fun disableRemoteVehicleCollisions(disable: Boolean) {
        requireOnline()
        nativeFunctionsExecutor.disableRemoteVehicleCollisions(playerid = id.value, disable = disable)
    }

    override var checkpoint: Checkpoint? = null
        set(value) {
            requireOnline()
            field = value
            when (value) {
                null -> nativeFunctionsExecutor.disablePlayerCheckpoint(id.value)
                else -> nativeFunctionsExecutor.setPlayerCheckpoint(
                        playerid = id.value,
                        x = value.coordinates.x,
                        y = value.coordinates.y,
                        z = value.coordinates.z,
                        size = value.size
                )
            }
        }

    override var raceCheckpoint: RaceCheckpoint? = null
        set(value) {
            requireOnline()
            field = value
            when (value) {
                null -> nativeFunctionsExecutor.disablePlayerRaceCheckpoint(id.value)
                else -> nativeFunctionsExecutor.setPlayerRaceCheckpoint(
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
        }

    override var worldBounds: Rectangle? = null
        set(value) {
            requireOnline()
            field = value
            nativeFunctionsExecutor.setPlayerWorldBounds(
                    playerid = id.value,
                    x_min = value?.minX ?: -20_000f,
                    x_max = value?.maxX ?: 20_000f,
                    y_min = value?.minY ?: -20_000f,
                    y_max = value?.maxY ?: 20_000f
            )
        }

    override fun showPlayerMarker(player: Player, color: Color) {
        requireOnline()
        player.requireOnline()
        nativeFunctionsExecutor.setPlayerMarkerForPlayer(playerid = this.id.value, showplayerid = player.id.value, color = color.value)
    }

    override fun showPlayerNameTag(player: Player, show: Boolean) {
        requireOnline()
        player.requireOnline()
        nativeFunctionsExecutor.showPlayerNameTagForPlayer(playerid = this.id.value, showplayerid = player.id.value, show = show)
    }

    internal val mapIconsById: MutableMap<PlayerMapIconId, PlayerMapIconImpl> = mutableMapOf()

    override val mapIcons: List<PlayerMapIcon>
        get() = mapIconsById.values.toList()

    override fun createMapIcon(playerMapIconId: PlayerMapIconId, coordinates: Vector3D, type: MapIconType, color: Color, style: MapIconStyle): PlayerMapIcon {
        requireOnline()
        mapIconsById[playerMapIconId]?.destroy()
        val playerMapIcon = PlayerMapIconImpl(
                player = this,
                id = playerMapIconId,
                nativeFunctionsExecutor = nativeFunctionsExecutor,
                coordinates = coordinates,
                type = type,
                color = color,
                style = style
        )
        mapIconsById[playerMapIconId] = playerMapIcon
        return playerMapIcon
    }

    override fun allowTeleport(allow: Boolean) {
        requireOnline()
        nativeFunctionsExecutor.allowPlayerTeleport(playerid = this.id.value, allow = allow)
    }

    override var cameraPosition: Vector3D
        get() {
            requireOnline()
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerCameraPos(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            requireOnline()
            nativeFunctionsExecutor.setPlayerCameraPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
        }

    override fun setCameraLookAt(coordinates: Vector3D, type: CameraType) {
        requireOnline()
        nativeFunctionsExecutor.setPlayerCameraLookAt(
                playerid = id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                cut = type.value
        )
    }

    override fun setCameraBehind() {
        requireOnline()
        nativeFunctionsExecutor.setCameraBehindPlayer(id.value)
    }

    override val cameraFrontVector: Vector3D
        get() {
            requireOnline()
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerCameraFrontVector(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }

    override val cameraMode: CameraMode
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerCameraMode(id.value).let { CameraMode[it] }
        }

    override fun enableCameraTarget(enable: Boolean) {
        requireOnline()
        nativeFunctionsExecutor.enablePlayerCameraTarget(playerid = id.value, enable = enable)
    }

    override val cameraTargetObject: MapObject?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerCameraTargetObject(id.value).let { mapObjectRegistry.getMapObject(it) }
        }

    override val cameraTargetVehicle: Vehicle?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerCameraTargetVehicle(id.value).let { vehicleRegistry.getVehicle(it) }
        }

    override val cameraTargetPlayer: Player?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerCameraTargetPlayer(id.value).let { playerRegistry.getPlayer(it) }
        }

    override val cameraTargetActor: Actor?
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerCameraTargetActor(id.value).let { actorRegistry.getActor(it) }
        }

    override val cameraAspectRatio: Float
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerCameraAspectRatio(id.value)
        }

    override val cameraZoom: Float
        get() {
            requireOnline()
            return nativeFunctionsExecutor.getPlayerCameraZoom(id.value)
        }

    override fun attachCameraToObject(mapObject: MapObject) {
        requireOnline()
        mapObject.requireNotDestroyed()
        nativeFunctionsExecutor.attachCameraToObject(playerid = id.value, objectid = mapObject.id.value)
    }

    override fun attachCameraToPlayerObject(playerMapObject: PlayerMapObject) {
        requireOnline()
        playerMapObject.requireNotDestroyed()
        nativeFunctionsExecutor.attachCameraToPlayerObject(playerid = id.value, playerobjectid = playerMapObject.id.value)
    }

    override fun interpolateCameraPosition(from: Vector3D, to: Vector3D, time: Int, type: CameraType) {
        requireOnline()
        nativeFunctionsExecutor.interpolateCameraPos(
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
        requireOnline()
        nativeFunctionsExecutor.interpolateCameraLookAt(
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

    override fun isInVehicle(vehicle: Vehicle): Boolean {
        requireOnline()
        vehicle.requireNotDestroyed()
        return nativeFunctionsExecutor.isPlayerInVehicle(playerid = id.value, vehicleid = vehicle.id.value)
    }

    override val isInAnyVehicle: Boolean
        get() {
            requireOnline()
            return nativeFunctionsExecutor.isPlayerInAnyVehicle(id.value)
        }

    override fun isInCheckpoint(checkpoint: Checkpoint): Boolean {
        requireOnline()
        return this.checkpoint == checkpoint && isInAnyCheckpoint
    }

    override val isInAnyCheckpoint: Boolean
        get() {
            requireOnline()
            return nativeFunctionsExecutor.isPlayerInCheckpoint(id.value)
        }

    override fun isInRaceCheckpoint(raceCheckpoint: RaceCheckpoint): Boolean {
        requireOnline()
        return this.raceCheckpoint == raceCheckpoint && isInAnyRaceCheckpoint
    }

    override val isInAnyRaceCheckpoint: Boolean
        get() {
            requireOnline()
            return nativeFunctionsExecutor.isPlayerInRaceCheckpoint(id.value)
        }

    override fun enableStuntBonus(enable: Boolean) {
        requireOnline()
        nativeFunctionsExecutor.enableStuntBonusForPlayer(playerid = id.value, enable = enable)
    }

    override fun spectate(player: Player, mode: SpectateType) {
        requireOnline()
        player.requireOnline()
        if (!isSpectating) {
            toggleSpectating(true)
        }
        nativeFunctionsExecutor.playerSpectatePlayer(
                playerid = this.id.value,
                targetplayerid = player.id.value,
                mode = mode.value
        )
    }

    override fun spectate(vehicle: Vehicle, mode: SpectateType) {
        requireOnline()
        vehicle.requireNotDestroyed()
        if (!isSpectating) {
            toggleSpectating(true)
        }
        nativeFunctionsExecutor.playerSpectateVehicle(
                playerid = this.id.value,
                targetvehicleid = vehicle.id.value,
                mode = mode.value
        )
    }

    override fun stopSpectating() {
        requireOnline()
        toggleSpectating(false)
    }

    private fun toggleSpectating(toggle: Boolean) {
        nativeFunctionsExecutor.togglePlayerSpectating(playerid = id.value, toggle = toggle)
        isSpectating = toggle
    }

    override var isSpectating: Boolean = false
        private set

    override fun startRecording(type: PlayerRecordingType, recordName: String) {
        requireOnline()
        nativeFunctionsExecutor.startRecordingPlayerData(
                playerid = id.value,
                recordtype = type.value,
                recordname = recordName
        )
    }

    override fun stopRecording() {
        requireOnline()
        nativeFunctionsExecutor.stopRecordingPlayerData(id.value)
    }

    override fun createExplosion(type: ExplosionType, area: Sphere) {
        requireOnline()
        nativeFunctionsExecutor.createExplosionForPlayer(
                playerid = id.value,
                X = area.x,
                Y = area.y,
                Z = area.z,
                type = type.value,
                Radius = area.radius
        )
    }

    override fun createExplosion(type: ExplosionType, coordinates: Vector3D, radius: Float) {
        requireOnline()
        nativeFunctionsExecutor.createExplosionForPlayer(
                playerid = id.value,
                X = coordinates.x,
                Y = coordinates.y,
                Z = coordinates.z,
                type = type.value,
                Radius = radius
        )
    }

    override val isAdmin: Boolean
        get() {
            requireOnline()
            return nativeFunctionsExecutor.isPlayerAdmin(id.value)
        }

    override val isNPC: Boolean by lazy {
        requireOnline()
        nativeFunctionsExecutor.isPlayerNPC(id.value)
    }

    override val isHuman: Boolean
        get() = !isNPC

    override fun kick() {
        requireOnline()
        nativeFunctionsExecutor.kick(id.value)
    }

    override fun ban(reason: String?) {
        requireOnline()
        when {
            reason != null && reason.isNotBlank() -> nativeFunctionsExecutor.banEx(playerid = id.value, reason = reason)
            else -> nativeFunctionsExecutor.ban(id.value)
        }
    }

    override val version: String
        get() {
            requireOnline()
            val version = ReferenceString()
            nativeFunctionsExecutor.getPlayerVersion(playerid = id.value, version = version, len = 24)
            return version.value ?: ""
        }

    override val networkStatisticsString: String
        get() {
            requireOnline()
            val networkStatisticsString = ReferenceString()
            nativeFunctionsExecutor.getPlayerNetworkStats(playerid = id.value, retstr = networkStatisticsString, size = 400)
            return networkStatisticsString.value ?: ""
        }

    override val networkStatistics: PlayerNetworkStatistics
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun selectTextDraw(hoverColor: Color) {
        requireOnline()
        nativeFunctionsExecutor.selectTextDraw(playerid = id.value, hovercolor = hoverColor.value)
    }

    override fun cancelSelectTextDraw() {
        requireOnline()
        nativeFunctionsExecutor.cancelSelectTextDraw(id.value)
    }

    override fun editMapObject(mapObject: MapObject) {
        requireOnline()
        mapObject.requireNotDestroyed()
        nativeFunctionsExecutor.editObject(playerid = id.value, objectid = mapObject.id.value)
    }

    override fun editPlayerMapObject(playerMapObject: PlayerMapObject) {
        requireOnline()
        playerMapObject.requireNotDestroyed()
        nativeFunctionsExecutor.editPlayerObject(playerid = id.value, objectid = playerMapObject.id.value)
    }

    override fun selectMapObject() {
        requireOnline()
        nativeFunctionsExecutor.selectObject(id.value)
    }

    override fun cancelSelectMapObject() {
        requireOnline()
        nativeFunctionsExecutor.cancelEdit(id.value)
    }

    override fun onSpawn(onSpawn: Player.() -> Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeath(onDeath: Player.(Player?, WeaponModel) -> Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val menu: Menu?
        get() {
            requireOnline()
            TODO("not implemented")
        } //To change initializer of created properties use File | Settings | File Templates.
}