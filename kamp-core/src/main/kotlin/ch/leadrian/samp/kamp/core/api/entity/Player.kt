package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.CrimeReport
import ch.leadrian.samp.kamp.core.api.constants.DefaultPlayerColors
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
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
import ch.leadrian.samp.kamp.core.api.constants.Weather
import ch.leadrian.samp.kamp.core.api.data.AngledLocation
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.LastShotVectors
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.PlayerKeys
import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.SpawnInfo
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Time
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogNavigation
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.core.api.entity.extension.Extendable
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.api.entity.id.TeamId
import ch.leadrian.samp.kamp.core.api.exception.PlayerOfflineException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerMapIconFactory
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerAngleProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerAngledLocationProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerArmourProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerCoordinatesProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerHealthProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerKeysProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerLastShotVectorsProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerLocationProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerNamePropertyFactory
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerPositionProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerTimeProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerVelocityProperty
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerMapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextDrawRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerTextLabelRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceString
import java.net.URL
import java.util.Collections
import java.util.Locale

class Player
internal constructor(
        id: PlayerId,
        private val actorRegistry: ActorRegistry,
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val mapObjectRegistry: MapObjectRegistry,
        private val menuRegistry: MenuRegistry,
        private val playerMapIconFactory: PlayerMapIconFactory,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        playerNamePropertyFactory: PlayerNamePropertyFactory
) : Entity<PlayerId>, Extendable<Player> {

    private val mapIconsById: MutableMap<PlayerMapIconId, PlayerMapIcon> = mutableMapOf()

    internal val playerMapObjectRegistry = PlayerMapObjectRegistry()

    internal val playerTextDrawRegistry = PlayerTextDrawRegistry()

    internal val playerTextLabelRegistry = PlayerTextLabelRegistry()

    override val extensions: EntityExtensionContainer<Player> = EntityExtensionContainer(this)

    override val id: PlayerId = id
        get() = requireConnected { field }

    var isConnected: Boolean = true
        private set

    var locale: Locale = Locale.getDefault()

    val dialogNavigation: DialogNavigation = DialogNavigation(this)

    val weapons: PlayerWeapons = PlayerWeapons(this, nativeFunctionExecutor)

    val camera: PlayerCamera = PlayerCamera(
            this,
            nativeFunctionExecutor,
            mapObjectRegistry,
            vehicleRegistry,
            playerRegistry,
            actorRegistry
    )

    val audioStream: PlayerAudioStream = PlayerAudioStream(this, nativeFunctionExecutor)

    val animation: PlayerAnimation = PlayerAnimation(this, nativeFunctionExecutor)

    var coordinates: Vector3D by PlayerCoordinatesProperty(nativeFunctionExecutor)

    var angle: Float by PlayerAngleProperty(nativeFunctionExecutor)

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

    var position: Position by PlayerPositionProperty(nativeFunctionExecutor)

    var location: Location by PlayerLocationProperty(nativeFunctionExecutor)

    var angledLocation: AngledLocation by PlayerAngledLocationProperty(nativeFunctionExecutor)

    var velocity: Vector3D by PlayerVelocityProperty(nativeFunctionExecutor)

    var health: Float by PlayerHealthProperty(nativeFunctionExecutor)

    var armour: Float by PlayerArmourProperty(nativeFunctionExecutor)

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

    var money: Int
        get() = nativeFunctionExecutor.getPlayerMoney(id.value)
        set(value) {
            val moneyToGive = value - nativeFunctionExecutor.getPlayerMoney(id.value)
            giveMoney(moneyToGive)
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

    var name: String by playerNamePropertyFactory.create()

    val state: PlayerState
        get() = nativeFunctionExecutor.getPlayerState(id.value).let { PlayerState[it] }

    val ping: Int
        get() = nativeFunctionExecutor.getPlayerPing(id.value)

    val keys: PlayerKeys by PlayerKeysProperty(nativeFunctionExecutor)

    var time: Time by PlayerTimeProperty(nativeFunctionExecutor)

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

    val lastShotVectors: LastShotVectors by PlayerLastShotVectorsProperty(nativeFunctionExecutor)

    val attachedObjectSlots: List<AttachedObjectSlot> =
            (0..9).map {
                AttachedObjectSlot(
                        player = this,
                        index = it,
                        nativeFunctionExecutor = nativeFunctionExecutor
                )
            }.let { Collections.unmodifiableList(it) }

    val playerVars: PlayerVars = PlayerVars(this, nativeFunctionExecutor)

    val vehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerVehicleID(id.value).let { vehicleRegistry[it] }

    val vehicleSeat: Int?
        get() = nativeFunctionExecutor.getPlayerVehicleSeat(id.value).takeIf { it != -1 }

    val isInAnyVehicle: Boolean
        get() = nativeFunctionExecutor.isPlayerInAnyVehicle(id.value)

    var specialAction: SpecialAction
        get() = nativeFunctionExecutor.getPlayerSpecialAction(id.value).let { SpecialAction[it] }
        set(value) {
            nativeFunctionExecutor.setPlayerSpecialAction(playerid = id.value, actionid = value.value)
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

    val isInAnyCheckpoint: Boolean
        get() = nativeFunctionExecutor.isPlayerInCheckpoint(id.value)

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

    val isInAnyRaceCheckpoint: Boolean
        get() = nativeFunctionExecutor.isPlayerInRaceCheckpoint(id.value)

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

    val mapIcons: List<PlayerMapIcon>
        get() = mapIconsById.values.toList()

    var isSpectating: Boolean = false
        private set

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

    val version: String by lazy {
        val version = ReferenceString()
        nativeFunctionExecutor.getPlayerVersion(playerid = id.value, version = version, len = 24)
        version.value ?: ""
    }

    val networkStatistics: PlayerNetworkStatistics = PlayerNetworkStatistics(
            player = this,
            nativeFunctionExecutor = nativeFunctionExecutor
    )

    val menu: Menu?
        get() = nativeFunctionExecutor.getPlayerMenu(id.value).let { menuRegistry[it] }

    val surfingVehicle: Vehicle?
        get() = nativeFunctionExecutor.getPlayerSurfingVehicleID(id.value).let { vehicleRegistry[it] }

    val surfingObject: MapObject?
        get() = nativeFunctionExecutor.getPlayerSurfingObjectID(id.value).let { mapObjectRegistry[it] }

    internal var currentDialog: Dialog? = null
        private set

    internal fun showDialog(
            dialog: Dialog,
            style: DialogStyle,
            button1: String,
            button2: String,
            caption: String,
            message: String
    ) {
        nativeFunctionExecutor.showPlayerDialog(
                playerid = id.value,
                dialogid = dialog.id.value,
                style = style.value,
                button1 = button1,
                button2 = button2,
                caption = caption,
                info = message
        )
        currentDialog = dialog
    }

    internal fun resetCurrentDialog() {
        currentDialog = null
    }

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

    fun setCoordinatesFindZ(coordinates: Vector3D) {
        nativeFunctionExecutor.setPlayerPosFindZ(
                playerid = id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z
        )
    }

    fun isStreamedIn(forPlayer: Player): Boolean =
            nativeFunctionExecutor.isPlayerStreamedIn(playerid = id.value, forplayerid = forPlayer.id.value)

    fun giveMoney(amount: Int) {
        nativeFunctionExecutor.givePlayerMoney(playerid = id.value, money = amount)
    }

    fun resetMoney() {
        nativeFunctionExecutor.resetPlayerMoney(id.value)
    }

    fun toggleClock(toggle: Boolean) {
        nativeFunctionExecutor.togglePlayerClock(playerid = id.value, toggle = toggle)
    }

    fun setWeather(weatherId: Int) {
        nativeFunctionExecutor.setPlayerWeather(playerid = id.value, weather = weatherId)
    }

    fun setWeather(weather: Weather) {
        setWeather(weather.value)
    }

    fun forceClassSelection() {
        nativeFunctionExecutor.forceClassSelection(id.value)
    }

    fun playCrimeReport(suspect: Player, crimeReport: CrimeReport) {
        nativeFunctionExecutor.playCrimeReportForPlayer(
                playerid = id.value,
                crime = crimeReport.value,
                suspectid = suspect.id.value
        )
    }

    fun setShopName(shopName: ShopName) {
        nativeFunctionExecutor.setPlayerShopName(playerid = id.value, shopname = shopName.value)
    }

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

    fun setChatBubble(text: String, color: Color, drawDistance: Float, expireTime: Int) {
        nativeFunctionExecutor.setPlayerChatBubble(
                playerid = id.value,
                text = text,
                color = color.value,
                drawdistance = drawDistance,
                expiretime = expireTime
        )
    }

    fun putInVehicle(vehicle: Vehicle, seat: Int) {
        nativeFunctionExecutor.putPlayerInVehicle(playerid = id.value, vehicleid = vehicle.id.value, seatid = seat)
    }

    fun removeFromVehicle(): Boolean =
            nativeFunctionExecutor.removePlayerFromVehicle(id.value)

    fun isInVehicle(vehicle: Vehicle): Boolean =
            nativeFunctionExecutor.isPlayerInVehicle(playerid = id.value, vehicleid = vehicle.id.value)

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

    fun disableRemoteVehicleCollisions(disable: Boolean) {
        nativeFunctionExecutor.disableRemoteVehicleCollisions(playerid = id.value, disable = disable)
    }

    fun isInCheckpoint(checkpoint: Checkpoint): Boolean {
        return this.checkpoint === checkpoint && isInAnyCheckpoint
    }

    fun isInRaceCheckpoint(raceCheckpoint: RaceCheckpoint): Boolean {
        return this.raceCheckpoint === raceCheckpoint && isInAnyRaceCheckpoint
    }

    fun showPlayerMarker(player: Player, color: Color) {
        nativeFunctionExecutor.setPlayerMarkerForPlayer(
                playerid = this.id.value,
                showplayerid = player.id.value,
                color = color.value
        )
    }

    fun showPlayerNameTag(player: Player, show: Boolean) {
        nativeFunctionExecutor.showPlayerNameTagForPlayer(
                playerid = this.id.value,
                showplayerid = player.id.value,
                show = show
        )
    }

    fun createMapIcon(
            playerMapIconId: PlayerMapIconId,
            coordinates: Vector3D,
            type: MapIconType,
            color: Color,
            style: MapIconStyle
    ): PlayerMapIcon {
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

    internal fun unregisterMapIcon(mapIcon: PlayerMapIcon) {
        mapIconsById.remove(mapIcon.id, mapIcon)
    }

    private fun destroyMapIcons() {
        mapIconsById.values.forEach { it.destroy() }
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

    fun kick() {
        nativeFunctionExecutor.kick(id.value)
    }

    @JvmOverloads
    fun ban(reason: String? = null) {
        when {
            reason != null && reason.isNotBlank() -> nativeFunctionExecutor.banEx(playerid = id.value, reason = reason)
            else -> nativeFunctionExecutor.ban(id.value)
        }
    }

    fun selectTextDraw(hoverColor: Color) {
        nativeFunctionExecutor.selectTextDraw(playerid = id.value, hovercolor = hoverColor.value)
    }

    fun cancelSelectTextDraw() {
        nativeFunctionExecutor.cancelSelectTextDraw(id.value)
    }

    fun selectMapObject() {
        nativeFunctionExecutor.selectObject(id.value)
    }

    fun cancelEditMapObject() {
        nativeFunctionExecutor.cancelEdit(id.value)
    }

    fun sendDeathMessage(victim: Player, weapon: WeaponModel, killer: Player? = null) {
        nativeFunctionExecutor.sendDeathMessageToPlayer(
                playerid = id.value,
                killer = killer?.id?.value ?: SAMPConstants.INVALID_PLAYER_ID,
                killee = victim.id.value,
                weapon = weapon.value
        )
    }

    internal fun onDisconnect() {
        if (!isConnected) {
            return
        }

        extensions.destroy()
        isConnected = false
        destroyMapIcons()
    }

    fun requireConnected(): Player {
        if (!isConnected) {
            throw PlayerOfflineException("Player is already offline")
        }
        return this
    }

    inline fun <T> requireConnected(block: Player.() -> T): T {
        requireConnected()
        return block(this)
    }

    inline fun <T : Any?> ifConnected(action: Player.() -> T): T? {
        if (isConnected) {
            return action.invoke(this)
        }
        return null
    }

    fun isInRange(coordinates: Vector3D, range: Float): Boolean {
        return nativeFunctionExecutor.isPlayerInRangeOfPoint(
                playerid = id.value,
                range = range,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z
        )
    }

    fun redirectDownload(url: String) {
        nativeFunctionExecutor.redirectDownload(id.value, url)
    }

    fun redirectDownload(url: URL) {
        redirectDownload(url.toString())
    }
}
