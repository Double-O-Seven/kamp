package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.*
import ch.leadrian.samp.kamp.api.data.*
import ch.leadrian.samp.kamp.api.entity.*
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.TeamId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.runtime.types.ReferenceFloat
import ch.leadrian.samp.kamp.runtime.types.ReferenceInt
import ch.leadrian.samp.kamp.runtime.types.ReferenceString
import java.util.*

class PlayerImpl(
        id: PlayerId,
        private val actorRegistry: ActorRegistry,
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val nativeFunctionsExecutor: SAMPNativeFunctionExecutor
) : Player {

    override var id: PlayerId = id
        private set

    override var isOnline: Boolean = true
        private set

    override var locale: Locale = Locale.getDefault()

    fun onDisconnect() {
        if (!isOnline) {
            throw IllegalStateException("Player cannot disconnect twice")
        }
        isOnline = false
        playerRegistry.unregister(this)
        this.id = PlayerId.INVALID
    }

    override fun spawn() {
        nativeFunctionsExecutor.spawnPlayer(id.value)
    }

    override fun setSpawnInfo(spawnInfo: SpawnInfo) {
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
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            return vector3DOf(x = x.value, y = y.value, z = z.value)
        }
        set(value) {
            nativeFunctionsExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
        }

    override var position: Position
        get() {
            val x = ReferenceFloat()
            val y = ReferenceFloat()
            val z = ReferenceFloat()
            val angle = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerPos(playerid = id.value, x = x, y = y, z = z)
            nativeFunctionsExecutor.getPlayerFacingAngle(playerid = id.value, angle = angle)
            return positionOf(x = x.value, y = y.value, z = z.value, angle = angle.value)
        }
        set(value) {
            nativeFunctionsExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
            nativeFunctionsExecutor.setPlayerFacingAngle(playerid = id.value, angle = value.angle)
        }

    override var location: Location
        get() {
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
            nativeFunctionsExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
            nativeFunctionsExecutor.setPlayerInterior(playerid = id.value, interiorid = value.interiorId)
            nativeFunctionsExecutor.setPlayerVirtualWorld(playerid = id.value, worldid = value.virtualWorldId)
        }

    override var angledLocation: AngledLocation
        get() {
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
            nativeFunctionsExecutor.setPlayerPos(playerid = id.value, x = value.x, y = value.y, z = value.z)
            nativeFunctionsExecutor.setPlayerInterior(playerid = id.value, interiorid = value.interiorId)
            nativeFunctionsExecutor.setPlayerVirtualWorld(playerid = id.value, worldid = value.virtualWorldId)
            nativeFunctionsExecutor.setPlayerFacingAngle(playerid = id.value, angle = value.angle)
        }

    override var angle: Float
        get() {
            val angle = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerFacingAngle(playerid = id.value, angle = angle)
            return angle.value
        }
        set(value) {
            nativeFunctionsExecutor.setPlayerFacingAngle(playerid = id.value, angle = value)
        }

    override var interiorId: Int
        get() = nativeFunctionsExecutor.getPlayerInterior(id.value)
        set(value) {
            nativeFunctionsExecutor.setPlayerInterior(playerid = id.value, interiorid = value)
        }

    override var virtualWorld: Int
        get() = nativeFunctionsExecutor.getPlayerVirtualWorld(id.value)
        set(value) {
            nativeFunctionsExecutor.setPlayerVirtualWorld(playerid = id.value, worldid = value)
        }

    override fun setPositionFindZ(coordinates: Vector3D) {
        nativeFunctionsExecutor.setPlayerPosFindZ(playerid = id.value, x = position.x, y = position.y, z = position.z)
    }

    override fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionsExecutor.isPlayerStreamedIn(playerid = id.value, forplayerid = forPlayer.id.value)

    override var health: Float
        get() {
            val health = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerHealth(playerid = id.value, health = health)
            return health.value
        }
        set(value) {
            nativeFunctionsExecutor.setPlayerHealth(playerid = id.value, health = value)
        }

    override var armour: Float
        get() {
            val armour = ReferenceFloat()
            nativeFunctionsExecutor.getPlayerArmour(playerid = id.value, armour = armour)
            return armour.value
        }
        set(value) {
            nativeFunctionsExecutor.setPlayerArmour(playerid = id.value, armour = value)
        }

    override fun setAmmo(weaponModel: WeaponModel, ammo: Int) {
        nativeFunctionsExecutor.setPlayerAmmo(playerid = id.value, weaponid = weaponModel.value, ammo = ammo)
    }

    override val ammo: Int
        get() = nativeFunctionsExecutor.getPlayerAmmo(id.value)

    override val weaponState: WeaponState
        get() = nativeFunctionsExecutor.getPlayerWeaponState(id.value).let { WeaponState[it] }

    override val targetPlayer: Player?
        get() = nativeFunctionsExecutor.getPlayerTargetPlayer(id.value).let { playerRegistry.getPlayer(it) }

    override val targetActor: Actor?
        get() = nativeFunctionsExecutor.getPlayerTargetActor(id.value).let { actorRegistry.getActor(it) }

    override var team: TeamId
        get() = nativeFunctionsExecutor.getPlayerTeam(id.value).let { TeamId.valueOf(it) }
        set(value) {
            nativeFunctionsExecutor.setPlayerTeam(playerid = id.value, teamid = value.value)
        }

    override var score: Int
        get() = nativeFunctionsExecutor.getPlayerScore(id.value)
        set(value) {
            nativeFunctionsExecutor.setPlayerScore(playerid = id.value, score = value)
        }

    override var drunkLevel: Int
        get() = nativeFunctionsExecutor.getPlayerDrunkLevel(id.value)
        set(value) {
            nativeFunctionsExecutor.setPlayerDrunkLevel(playerid = id.value, level = value)
        }

    override var color: Color
        get() = nativeFunctionsExecutor.getPlayerColor(id.value).let { colorOf(it) }
        set(value) {
            nativeFunctionsExecutor.setPlayerColor(playerid = id.value, color = value.value)
        }

    override var skin: SkinModel
        get() = nativeFunctionsExecutor.getPlayerSkin(id.value).let { SkinModel[it] }
        set(value) {
            nativeFunctionsExecutor.setPlayerSkin(playerid = id.value, skinid = value.value)
        }

    override var armedWeapon: WeaponModel
        get() = nativeFunctionsExecutor.getPlayerWeapon(id.value).let { WeaponModel[it] }
        set(value) {
            nativeFunctionsExecutor.setPlayerArmedWeapon(playerid = id.value, weaponid = value.value)
        }

    override fun getWeaponData(slot: WeaponSlot): WeaponData {
        val weapon = ReferenceInt()
        val ammo = ReferenceInt()
        nativeFunctionsExecutor.getPlayerWeaponData(playerid = id.value, slot = slot.value, weapon = weapon, ammo = ammo)
        return weaponDataOf(model = WeaponModel[weapon.value], ammo = ammo.value)
    }

    override var money: Int
        get() = nativeFunctionsExecutor.getPlayerMoney(id.value)
        set(value) {
            val moneyToGive = value - nativeFunctionsExecutor.getPlayerMoney(id.value)
            nativeFunctionsExecutor.givePlayerMoney(playerid = id.value, money = moneyToGive)
        }

    override fun giveMoney(amount: Int) {
        nativeFunctionsExecutor.givePlayerMoney(playerid = id.value, money = amount)
    }

    override fun resetMoney() {
        nativeFunctionsExecutor.resetPlayerMoney(id.value)
    }

    override val ipAddress: String by lazy {
        val ipAddress = ReferenceString()
        nativeFunctionsExecutor.getPlayerIp(playerid = id.value, ip = ipAddress, size = 16)
        ipAddress.value ?: "255.255.255.255"
    }

    override var name: String
        get() {
            val name = ReferenceString()
            nativeFunctionsExecutor.getPlayerName(playerid = id.value, name = name, size = SAMPConstants.MAX_PLAYER_NAME)
            return name.value.orEmpty()
        }
        set(value) {
            nativeFunctionsExecutor.setPlayerName(playerid = id.value, name = value)
        }

    override val state: PlayerState
        get() = nativeFunctionsExecutor.getPlayerState(id.value).let { PlayerState[it] }

    override val ping: Int
        get() = nativeFunctionsExecutor.getPlayerPing(id.value)

    override val keys: PlayerKeys
        get() {
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
            val hour = ReferenceInt()
            val minute = ReferenceInt()
            nativeFunctionsExecutor.getPlayerTime(playerid = id.value, hour = hour, minute = minute)
            return timeOf(hour = hour.value, minute = minute.value)
        }
        set(value) {
            nativeFunctionsExecutor.setPlayerTime(playerid = id.value, hour = value.hour, minute = value.minute)
        }

    override fun toggleClock(toggle: Boolean) {
        nativeFunctionsExecutor.togglePlayerClock(playerid = id.value, toggle = toggle)
    }

    override fun setWeather(weatherId: Int) {
        nativeFunctionsExecutor.setPlayerWeather(playerid = id.value, weather = weatherId)
    }

    override fun forceClassSelection() {
        nativeFunctionsExecutor.forceClassSelection(id.value)
    }

    override var wantedLevel: Int
        get() = nativeFunctionsExecutor.getPlayerWantedLevel(id.value)
        set(value) {
            nativeFunctionsExecutor.setPlayerWantedLevel(playerid = id.value, level = value)
        }

    override var fightingStyle: FightingStyle
        get() = nativeFunctionsExecutor.getPlayerFightingStyle(id.value).let { FightingStyle[it] }
        set(value) {
            nativeFunctionsExecutor.setPlayerFightingStyle(playerid = id.value, style = value.value)
        }

    override fun playCrimeReport(suspect: Player, crimeReport: CrimeReport) {
        nativeFunctionsExecutor.playCrimeReportForPlayer(
                playerid = id.value,
                crime = crimeReport.value,
                suspectid = suspect.id.value
        )
    }

    override fun playAudioStream(url: String, position: Sphere, usePosition: Boolean) {
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
        nativeFunctionsExecutor.stopAudioStreamForPlayer(id.value)
    }

    override fun setShopName(shopName: ShopName) {
        nativeFunctionsExecutor.setPlayerShopName(playerid = id.value, shopname = shopName.value)
    }

    override fun setSkillLevel(skill: WeaponSkill, level: Int) {
        nativeFunctionsExecutor.setPlayerSkillLevel(playerid = id.value, skill = skill.value, level = level)
    }

    override val surfingVehicle: Vehicle?
        get() = nativeFunctionsExecutor.getPlayerSurfingVehicleID(id.value).let { vehicleRegistry.getVehicle(it) }

    override val surfingObject: MapObject?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun removeBuilding(modelId: Int, position: Sphere) {
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

    override fun getAttachedObjectSlot(index: Int): AttachedObjectSlot {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val playerVars: PlayerVars = PlayerVarsImpl(this, nativeFunctionsExecutor)

    override fun setChatBubble(text: String, color: Color, drawDistance: Float, expireTime: Int) {
        nativeFunctionsExecutor.setPlayerChatBubble(
                playerid = id.value,
                text = text,
                color = color.value,
                drawdistance = drawDistance,
                expiretime = expireTime
        )
    }

    override val vehicle: Vehicle?
        get() = nativeFunctionsExecutor.getPlayerVehicleID(id.value).let { vehicleRegistry.getVehicle(it) }

    override val vehicleSeat: Int?
        get() = nativeFunctionsExecutor.getPlayerVehicleSeat(id.value).takeIf { it != -1 }

    override fun removeFromVehicle() {
        nativeFunctionsExecutor.removePlayerFromVehicle(id.value)
    }

    override fun toggleControllable(toggle: Boolean) {
        nativeFunctionsExecutor.togglePlayerControllable(playerid = id.value, toggle = toggle)
    }

    override fun playSound(soundId: Int, coordinates: Vector3D) {
        nativeFunctionsExecutor.playerPlaySound(
                playerid = id.value,
                soundid = soundId,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z
        )
    }

    override fun applyAnimation(animation: Animation, fDelta: Float, loop: Boolean, lockX: Boolean, lockY: Boolean, freeze: Boolean, time: Int, forceSync: Boolean) {
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
        nativeFunctionsExecutor.clearAnimations(playerid = id.value, forcesync = forceSync)
    }

    override val animationIndex: Int
        get() = nativeFunctionsExecutor.getPlayerAnimationIndex(id.value)

    override var specialAction: SpecialAction
        get() = nativeFunctionsExecutor.getPlayerSpecialAction(id.value).let { SpecialAction[it] }
        set(value) {
            nativeFunctionsExecutor.setPlayerSpecialAction(playerid = id.value, actionid = value.value)
        }

    override fun disableRemoteVehicleCollisions(disable: Boolean) {
        nativeFunctionsExecutor.disableRemoteVehicleCollisions(playerid = id.value, disable = disable)
    }

    override var checkpoint: Checkpoint?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override var raceCheckpoint: RaceCheckpoint?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override var worldBounds: Rectangle?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun showPlayerMarker(player: Player, color: Color) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showPlayerNameTag(player: Player, show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val mapIcon: PlayerMapIcon?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun allowTeleport(allow: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var cameraPosition: Vector3D
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun setCameraLookAt(coordinates: Vector3D, type: CameraType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCameraBehind() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val cameraFrontVector: Vector3D
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val cameraMode: CameraMode
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun enableCameraTarget(enable: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val cameraTargetObject: MapObject?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val cameraTargetVehicle: Vehicle?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val cameraTargetPlayer: Player?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val cameraTargetActor: Actor?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val cameraAspectRatio: Float
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val cameraZoom: Float
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun attachCameraToObject(mapObject: MapObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun attachCameraToPlayerObject(playerMapObject: PlayerMapObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun interpolateCameraPosition(from: Vector3D, to: Vector3D, time: Int, type: CameraType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun interpolateCameraLookAt(from: Vector3D, to: Vector3D, time: Int, type: CameraType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isInVehicle(vehicle: Vehicle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isInAnyVehicle: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun isInCheckpoint(checkpoint: Checkpoint) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isInAnyCheckpoint: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun isInRaceCheckpoint(raceCheckpoint: RaceCheckpoint) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isInAnyRaceCheckpoint: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override var virtualWorldId: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun enableStuntBonus(enable: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun spectate(player: Player, mode: SpectateType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun spectate(vehicle: Vehicle, mode: SpectateType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopSpectating() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startRecording(type: PlayerRecordingType, recordName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopRecording() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createExplosion(type: ExplosionType, area: Sphere) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createExplosion(type: ExplosionType, coordinates: Vector3D, radius: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isAdmin: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val isNPC: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val isHuman: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun kick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ban(reason: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val version: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val networkStatistics: PlayerNetworkStatistics
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun selectTextDraw(hoverColor: Color) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancelSelectTextDraw() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editMapObject(mapObject: MapObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editPlayerMapObject(playerMapObject: PlayerMapObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectMapObject() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancelSelectMapObject() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSpawn(onSpawn: Player.() -> Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeath(onDeath: Player.(Player?, WeaponModel) -> Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val menu: Menu?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}