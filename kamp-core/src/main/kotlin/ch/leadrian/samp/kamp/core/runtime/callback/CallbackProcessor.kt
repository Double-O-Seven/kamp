package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickMapListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestClassListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTextListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener
import ch.leadrian.samp.kamp.core.api.callback.OnRconCommandListener
import ch.leadrian.samp.kamp.core.api.callback.OnTrailerUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnUnoccupiedVehicleUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleModListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.Bone
import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.constants.ClickPlayerSource
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.DownloadRequestType
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.MapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.NoHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerHitTarget
import ch.leadrian.samp.kamp.core.api.data.PlayerMapObjectHitTarget
import ch.leadrian.samp.kamp.core.api.data.VehicleHitTarget
import ch.leadrian.samp.kamp.core.api.data.playerKeysOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.UncaughtExceptionNotifier
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.core.runtime.SAMPCallbacks
import ch.leadrian.samp.kamp.core.runtime.Server
import ch.leadrian.samp.kamp.core.runtime.amx.AmxCallbackExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerClassRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CallbackProcessor
@Inject
constructor(
        private val server: Server,
        private val playerFactory: PlayerFactory,
        private val playerRegistry: PlayerRegistry,
        private val vehicleRegistry: VehicleRegistry,
        private val actorRegistry: ActorRegistry,
        private val playerClassRegistry: PlayerClassRegistry,
        private val mapObjectRegistry: MapObjectRegistry,
        private val pickupRegistry: PickupRegistry,
        private val textDrawRegistry: TextDrawRegistry,
        private val onActorStreamInHandler: OnActorStreamInHandler,
        private val onActorStreamOutHandler: OnActorStreamOutHandler,
        private val onDialogResponseHandler: OnDialogResponseHandler,
        private val onEnterExitModShopHandler: OnEnterExitModShopHandler,
        private val onGameModeExitHandler: OnGameModeExitHandler,
        private val onGameModeInitHandler: OnGameModeInitHandler,
        private val onIncomingConnectionHandler: OnIncomingConnectionHandler,
        private val onMapObjectMovedHandler: OnMapObjectMovedHandler,
        private val onPlayerClickMapHandler: OnPlayerClickMapHandler,
        private val onPlayerClickPlayerHandler: OnPlayerClickPlayerHandler,
        private val onPlayerClickPlayerTextDrawHandler: OnPlayerClickPlayerTextDrawHandler,
        private val onPlayerClickTextDrawHandler: OnPlayerClickTextDrawHandler,
        private val onPlayerCancelTextDrawSelectionHandler: OnPlayerCancelTextDrawSelectionHandler,
        private val onPlayerCommandTextHandler: OnPlayerCommandTextHandler,
        private val onPlayerConnectHandler: OnPlayerConnectHandler,
        private val onPlayerDeathHandler: OnPlayerDeathHandler,
        private val onPlayerDisconnectHandler: OnPlayerDisconnectHandler,
        private val onPlayerEditAttachedObjectHandler: OnPlayerEditAttachedObjectHandler,
        private val onPlayerEditMapObjectHandler: OnPlayerEditMapObjectHandler,
        private val onPlayerEditPlayerMapObjectHandler: OnPlayerEditPlayerMapObjectHandler,
        private val onPlayerEnterCheckpointHandler: OnPlayerEnterCheckpointHandler,
        private val onPlayerEnterRaceCheckpointHandler: OnPlayerEnterRaceCheckpointHandler,
        private val onPlayerEnterVehicleHandler: OnPlayerEnterVehicleHandler,
        private val onPlayerExitedMenuHandler: OnPlayerExitedMenuHandler,
        private val onPlayerExitVehicleHandler: OnPlayerExitVehicleHandler,
        private val onPlayerGiveDamageActorHandler: OnPlayerGiveDamageActorHandler,
        private val onPlayerGiveDamageHandler: OnPlayerGiveDamageHandler,
        private val onPlayerInteriorChangeHandler: OnPlayerInteriorChangeHandler,
        private val onPlayerKeyStateChangeHandler: OnPlayerKeyStateChangeHandler,
        private val onPlayerLeaveCheckpointHandler: OnPlayerLeaveCheckpointHandler,
        private val onPlayerLeaveRaceCheckpointHandler: OnPlayerLeaveRaceCheckpointHandler,
        private val onPlayerMapObjectMovedHandler: OnPlayerMapObjectMovedHandler,
        private val onPlayerPickUpPickupHandler: OnPlayerPickUpPickupHandler,
        private val onPlayerRequestClassHandler: OnPlayerRequestClassHandler,
        private val onPlayerRequestSpawnHandler: OnPlayerRequestSpawnHandler,
        private val onPlayerSelectedMenuRowHandler: OnPlayerSelectedMenuRowHandler,
        private val onPlayerSelectMapObjectHandler: OnPlayerSelectMapObjectHandler,
        private val onPlayerSelectPlayerMapObjectHandler: OnPlayerSelectPlayerMapObjectHandler,
        private val onPlayerSpawnHandler: OnPlayerSpawnHandler,
        private val onPlayerStateChangeHandler: OnPlayerStateChangeHandler,
        private val onPlayerStreamInHandler: OnPlayerStreamInHandler,
        private val onPlayerStreamOutHandler: OnPlayerStreamOutHandler,
        private val onPlayerTakeDamageHandler: OnPlayerTakeDamageHandler,
        private val onPlayerTextHandler: OnPlayerTextHandler,
        private val onPlayerUpdateHandler: OnPlayerUpdateHandler,
        private val onPlayerWeaponShotHandler: OnPlayerWeaponShotHandler,
        private val onProcessTickHandler: OnProcessTickHandler,
        private val onRconCommandHandler: OnRconCommandHandler,
        private val onRconLoginAttemptHandler: OnRconLoginAttemptHandler,
        private val onTrailerUpdateHandler: OnTrailerUpdateHandler,
        private val onUnoccupiedVehicleUpdateHandler: OnUnoccupiedVehicleUpdateHandler,
        private val onVehicleDamageStatusUpdateHandler: OnVehicleDamageStatusUpdateHandler,
        private val onVehicleDeathHandler: OnVehicleDeathHandler,
        private val onVehicleModHandler: OnVehicleModHandler,
        private val onVehiclePaintjobHandler: OnVehiclePaintjobHandler,
        private val onVehicleResprayHandler: OnVehicleResprayHandler,
        private val onVehicleSirenStateChangeHandler: OnVehicleSirenStateChangeHandler,
        private val onVehicleSpawnHandler: OnVehicleSpawnHandler,
        private val onVehicleStreamInHandler: OnVehicleStreamInHandler,
        private val onVehicleStreamOutHandler: OnVehicleStreamOutHandler,
        private val onPlayerRequestDownloadHandler: OnPlayerRequestDownloadHandler,
        private val amxCallbackExecutor: AmxCallbackExecutor
) : SAMPCallbacks {

    private companion object {

        val log = loggerFor<CallbackProcessor>()
    }

    @com.google.inject.Inject(optional = true)
    private var uncaughtExceptionNotifier: UncaughtExceptionNotifier? = null

    private inline fun <T> tryAndCatch(block: () -> T): T? {
        return try {
            block()
        } catch (e: Exception) {
            log.error("Exception in callback", e)
            notifyAboutException(e)
            null
        }
    }

    private fun notifyAboutException(exception: Exception) {
        try {
            uncaughtExceptionNotifier?.notify(exception)
        } catch (e: Exception) {
            log.error("Exception while notifying {} about exception", uncaughtExceptionNotifier, e)
        }
    }

    private fun Int.toPlayer(): Player =
            playerRegistry[this] ?: throw IllegalArgumentException("Invalid player ID $this")

    private fun Int.toPlayerOrNull(): Player? = playerRegistry[this]

    private fun Int.toPlayerClass(): PlayerClass =
            playerClassRegistry[this] ?: throw IllegalArgumentException("Invalid player class ID $this")

    private fun Int.toVehicle(): Vehicle =
            vehicleRegistry[this] ?: throw IllegalArgumentException("Invalid vehicle ID $this")

    private fun Int.toMapObject(): MapObject =
            mapObjectRegistry[this] ?: throw IllegalArgumentException("Invalid map object ID $this")

    private fun Int.toPlayerMapObject(player: Player): PlayerMapObject =
            player.playerMapObjectRegistry[this]
                    ?: throw IllegalArgumentException("Invalid player map object ID $this for player ID ${player.id.value}")

    private fun Int.toPickup(): Pickup =
            pickupRegistry[this] ?: throw IllegalArgumentException("Invalid pickup ID $this")

    private fun Int.toActor(): Actor =
            actorRegistry[this] ?: throw IllegalArgumentException("Invalid actor ID $this")

    private fun Int.toTextDrawOrNull(): TextDraw? = textDrawRegistry[this]

    private fun Int.toPlayerTextDraw(player: Player): PlayerTextDraw =
            player.playerTextDrawRegistry[this]
                    ?: throw IllegalArgumentException("Invalid player text draw ID $this for player ID ${player.id.value}")

    override fun onProcessTick() {
        tryAndCatch {
            onProcessTickHandler.onProcessTick()
        }
    }

    override fun onPublicCall(name: String, paramsAddress: Int, heapPointer: Int): Int? {
        return tryAndCatch {
            amxCallbackExecutor.onPublicCall(name, paramsAddress, heapPointer)
        }
    }

    override fun onGameModeInit(): Boolean {
        tryAndCatch {
            onGameModeInitHandler.onGameModeInit()
        }
        return true
    }

    override fun onGameModeExit(): Boolean {
        tryAndCatch {
            onGameModeExitHandler.onGameModeExit()
        }
        tryAndCatch {
            server.stop()
        }
        return true
    }

    override fun onPlayerConnect(playerid: Int): Boolean {
        tryAndCatch {
            val player = playerFactory.create(PlayerId.valueOf(playerid))
            onPlayerConnectHandler.onPlayerConnect(player)
        }
        return true
    }

    override fun onPlayerDisconnect(playerid: Int, reason: Int): Boolean {
        tryAndCatch {
            val player = playerid.toPlayer()
            onPlayerDisconnectHandler.onPlayerDisconnect(player, DisconnectReason[reason])
            player.onDisconnect()
        }
        return true
    }

    override fun onPlayerSpawn(playerid: Int): Boolean {
        tryAndCatch {
            onPlayerSpawnHandler.onPlayerSpawn(playerid.toPlayer())
        }
        return true
    }

    override fun onPlayerDeath(playerid: Int, killerid: Int, reason: Int): Boolean {
        tryAndCatch {
            onPlayerDeathHandler.onPlayerDeath(
                    player = playerid.toPlayer(),
                    killer = killerid.toPlayerOrNull(),
                    reason = WeaponModel[reason]
            )
        }
        return true
    }

    override fun onVehicleSpawn(vehicleid: Int): Boolean {
        tryAndCatch {
            val vehicle = vehicleid.toVehicle()
            onVehicleSpawnHandler.onVehicleSpawn(vehicle)
        }
        return true
    }

    override fun onVehicleDeath(vehicleid: Int, killerid: Int): Boolean {
        tryAndCatch {
            onVehicleDeathHandler.onVehicleDeath(vehicleid.toVehicle(), killerid.toPlayerOrNull())
        }
        return true
    }

    override fun onPlayerText(playerid: Int, text: String): Boolean {
        val result = tryAndCatch {
            onPlayerTextHandler.onPlayerText(playerid.toPlayer(), text)
        } ?: OnPlayerTextListener.Result.Allowed
        return result.value
    }

    override fun onPlayerCommandText(playerid: Int, cmdtext: String): Boolean {
        val result = tryAndCatch {
            onPlayerCommandTextHandler.onPlayerCommandText(playerid.toPlayer(), cmdtext)
        } ?: OnPlayerCommandTextListener.Result.UnknownCommand
        return result.value
    }

    override fun onPlayerRequestClass(playerid: Int, classid: Int): Boolean {
        val result = tryAndCatch {
            onPlayerRequestClassHandler.onPlayerRequestClass(playerid.toPlayer(), classid.toPlayerClass())
        } ?: OnPlayerRequestClassListener.Result.Allow
        return result.value
    }

    override fun onPlayerEnterVehicle(playerid: Int, vehicleid: Int, ispassenger: Boolean): Boolean {
        tryAndCatch {
            onPlayerEnterVehicleHandler.onPlayerEnterVehicle(playerid.toPlayer(), vehicleid.toVehicle(), ispassenger)
        }
        return true
    }

    override fun onPlayerExitVehicle(playerid: Int, vehicleid: Int): Boolean {
        tryAndCatch {
            onPlayerExitVehicleHandler.onPlayerExitVehicle(playerid.toPlayer(), vehicleid.toVehicle())
        }
        return true
    }

    override fun onPlayerStateChange(playerid: Int, newstate: Int, oldstate: Int): Boolean {
        tryAndCatch {
            onPlayerStateChangeHandler.onPlayerStateChange(
                    player = playerid.toPlayer(),
                    newState = PlayerState[newstate],
                    oldState = PlayerState[oldstate]
            )
        }
        return true
    }

    override fun onPlayerEnterCheckpoint(playerid: Int): Boolean {
        tryAndCatch {
            onPlayerEnterCheckpointHandler.onPlayerEnterCheckpoint(playerid.toPlayer())
        }
        return true
    }

    override fun onPlayerLeaveCheckpoint(playerid: Int): Boolean {
        tryAndCatch {
            onPlayerLeaveCheckpointHandler.onPlayerLeaveCheckpoint(playerid.toPlayer())
        }
        return true
    }

    override fun onPlayerEnterRaceCheckpoint(playerid: Int): Boolean {
        tryAndCatch {
            onPlayerEnterRaceCheckpointHandler.onPlayerEnterRaceCheckpoint(playerid.toPlayer())
        }
        return true
    }

    override fun onPlayerLeaveRaceCheckpoint(playerid: Int): Boolean {
        tryAndCatch {
            onPlayerLeaveRaceCheckpointHandler.onPlayerLeaveRaceCheckpoint(playerid.toPlayer())
        }
        return true
    }

    override fun onRconCommand(cmd: String): Boolean {
        val result = tryAndCatch {
            onRconCommandHandler.onRconCommand(cmd)
        } ?: OnRconCommandListener.Result.UnknownCommand
        return result.value
    }

    override fun onPlayerRequestSpawn(playerid: Int): Boolean {
        val result = tryAndCatch {
            onPlayerRequestSpawnHandler.onPlayerRequestSpawn(playerid.toPlayer())
        } ?: OnPlayerRequestSpawnListener.Result.Denied
        return result.value
    }

    override fun onObjectMoved(objectid: Int): Boolean {
        tryAndCatch {
            onMapObjectMovedHandler.onMapObjectMoved(objectid.toMapObject())
        }
        return true
    }

    override fun onPlayerObjectMoved(playerid: Int, objectid: Int): Boolean {
        tryAndCatch {
            val player = playerid.toPlayer()
            onPlayerMapObjectMovedHandler.onPlayerMapObjectMoved(objectid.toPlayerMapObject(player))
        }
        return true
    }

    override fun onPlayerPickUpPickup(playerid: Int, pickupid: Int): Boolean {
        tryAndCatch {
            onPlayerPickUpPickupHandler.onPlayerPickUpPickup(playerid.toPlayer(), pickupid.toPickup())
        }
        return true
    }

    override fun onVehicleMod(playerid: Int, vehicleid: Int, componentid: Int): Boolean {
        val result = tryAndCatch {
            onVehicleModHandler.onVehicleMod(
                    playerid.toPlayer(),
                    vehicleid.toVehicle(),
                    VehicleComponentModel[componentid]
            )
        } ?: OnVehicleModListener.Result.Desync
        return result.value
    }

    override fun onEnterExitModShop(playerid: Int, enterexit: Boolean, interiorid: Int): Boolean {
        tryAndCatch {
            onEnterExitModShopHandler.onEnterExitModShop(playerid.toPlayer(), enterexit, interiorid)
        }
        return true
    }

    override fun onVehiclePaintjob(playerid: Int, vehicleid: Int, paintjobid: Int): Boolean {
        tryAndCatch {
            onVehiclePaintjobHandler.onVehiclePaintjob(playerid.toPlayer(), vehicleid.toVehicle(), paintjobid)
        }
        return true
    }

    override fun onVehicleRespray(playerid: Int, vehicleid: Int, color1: Int, color2: Int): Boolean {
        val result = tryAndCatch {
            onVehicleResprayHandler.onVehicleRespray(
                    playerid.toPlayer(),
                    vehicleid.toVehicle(),
                    vehicleColorsOf(color1, color2)
            )
        } ?: OnVehicleResprayListener.Result.Desync
        return result.value
    }

    override fun onVehicleDamageStatusUpdate(vehicleid: Int, playerid: Int): Boolean {
        tryAndCatch {
            onVehicleDamageStatusUpdateHandler.onVehicleDamageStatusUpdate(vehicleid.toVehicle(), playerid.toPlayer())
        }
        return true
    }

    override fun onUnoccupiedVehicleUpdate(
            vehicleid: Int,
            playerid: Int,
            passenger_seat: Int,
            new_x: Float,
            new_y: Float,
            new_z: Float,
            vel_x: Float,
            vel_y: Float,
            vel_z: Float
    ): Boolean {
        val result = tryAndCatch {
            onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                    vehicle = vehicleid.toVehicle(),
                    player = playerid.toPlayer(),
                    passengerSeat = passenger_seat.takeIf { it != 0 },
                    coordinates = vector3DOf(x = new_x, y = new_y, z = new_z),
                    velocity = vector3DOf(x = vel_x, y = vel_y, z = vel_z)
            )
        } ?: OnUnoccupiedVehicleUpdateListener.Result.Sync
        return result.value
    }

    override fun onPlayerSelectedMenuRow(playerid: Int, row: Int): Boolean {
        tryAndCatch {
            val player = playerid.toPlayer()
            val menuRow = player.menu?.rows?.getOrNull(row)
            if (menuRow != null) {
                onPlayerSelectedMenuRowHandler.onPlayerSelectedMenuRow(player, menuRow)
            }
        }
        return true
    }

    override fun onPlayerExitedMenu(playerid: Int): Boolean {
        tryAndCatch {
            val player = playerid.toPlayer()
            val menu = player.menu
            if (menu != null) {
                onPlayerExitedMenuHandler.onPlayerExitedMenu(player, menu)
            }
        }
        return true
    }

    override fun onPlayerInteriorChange(playerid: Int, newinteriorid: Int, oldinteriorid: Int): Boolean {
        tryAndCatch {
            onPlayerInteriorChangeHandler.onPlayerInteriorChange(
                    player = playerid.toPlayer(),
                    newInteriorId = newinteriorid,
                    oldInteriorId = oldinteriorid
            )
        }
        return true
    }

    override fun onPlayerKeyStateChange(playerid: Int, newkeys: Int, oldkeys: Int): Boolean {
        tryAndCatch {
            onPlayerKeyStateChangeHandler.onPlayerKeyStateChange(
                    player = playerid.toPlayer(),
                    oldKeys = playerKeysOf(
                            keys = oldkeys,
                            upDown = 0,
                            leftRight = 0
                    ),
                    newKeys = playerKeysOf(
                            keys = newkeys,
                            upDown = 0,
                            leftRight = 0
                    )
            )
        }
        return true
    }

    override fun onRconLoginAttempt(ip: String, password: String, success: Boolean): Boolean {
        tryAndCatch {
            onRconLoginAttemptHandler.onRconLoginAttempt(ipAddress = ip, password = password, success = success)
        }
        return true
    }

    override fun onPlayerUpdate(playerid: Int): Boolean {
        val result = tryAndCatch {
            onPlayerUpdateHandler.onPlayerUpdate(playerid.toPlayer())
        } ?: OnPlayerUpdateListener.Result.Sync
        return result.value
    }

    override fun onPlayerStreamIn(playerid: Int, forplayerid: Int): Boolean {
        tryAndCatch {
            onPlayerStreamInHandler.onPlayerStreamIn(player = playerid.toPlayer(), forPlayer = forplayerid.toPlayer())
        }
        return true
    }

    override fun onPlayerStreamOut(playerid: Int, forplayerid: Int): Boolean {
        tryAndCatch {
            onPlayerStreamOutHandler.onPlayerStreamOut(player = playerid.toPlayer(), forPlayer = forplayerid.toPlayer())
        }
        return true
    }

    override fun onVehicleStreamIn(vehicleid: Int, forplayerid: Int): Boolean {
        tryAndCatch {
            onVehicleStreamInHandler.onVehicleStreamIn(vehicleid.toVehicle(), forplayerid.toPlayer())
        }
        return true
    }

    override fun onVehicleStreamOut(vehicleid: Int, forplayerid: Int): Boolean {
        tryAndCatch {
            onVehicleStreamOutHandler.onVehicleStreamOut(vehicleid.toVehicle(), forplayerid.toPlayer())
        }
        return true
    }

    override fun onActorStreamIn(actorid: Int, forplayerid: Int): Boolean {
        tryAndCatch {
            onActorStreamInHandler.onActorStreamIn(actorid.toActor(), forplayerid.toPlayer())
        }
        return true
    }

    override fun onActorStreamOut(actorid: Int, forplayerid: Int): Boolean {
        tryAndCatch {
            onActorStreamOutHandler.onActorStreamOut(actorid.toActor(), forplayerid.toPlayer())
        }
        return true
    }

    override fun onDialogResponse(
            playerid: Int,
            dialogid: Int,
            response: Int,
            listitem: Int,
            inputtext: String
    ): Boolean {
        val result = tryAndCatch {
            onDialogResponseHandler.onDialogResponse(
                    playerid.toPlayer(),
                    DialogId.valueOf(dialogid),
                    DialogResponse[response],
                    listitem,
                    inputtext
            )
        } ?: OnDialogResponseListener.Result.Ignored
        return result.value
    }

    override fun onPlayerTakeDamage(
            playerid: Int,
            issuerid: Int,
            amount: Float,
            weaponid: Int,
            bodypart: Int
    ): Boolean {
        tryAndCatch {
            onPlayerTakeDamageHandler.onPlayerTakeDamage(
                    player = playerid.toPlayer(),
                    issuer = issuerid.toPlayerOrNull(),
                    amount = amount,
                    weaponModel = WeaponModel[weaponid],
                    bodyPart = BodyPart[bodypart]
            )
        }
        return true
    }

    override fun onPlayerGiveDamage(
            playerid: Int,
            damagedid: Int,
            amount: Float,
            weaponid: Int,
            bodypart: Int
    ): Boolean {
        tryAndCatch {
            onPlayerGiveDamageHandler.onPlayerGiveDamage(
                    player = playerid.toPlayer(),
                    damagedPlayer = damagedid.toPlayer(),
                    amount = amount,
                    weaponModel = WeaponModel[weaponid],
                    bodyPart = BodyPart[bodypart]
            )
        }
        return true
    }

    override fun onPlayerGiveDamageActor(
            playerid: Int,
            damaged_actorid: Int,
            amount: Float,
            weaponid: Int,
            bodypart: Int
    ): Boolean {
        tryAndCatch {
            onPlayerGiveDamageActorHandler.onPlayerGiveDamageActor(
                    player = playerid.toPlayer(),
                    actor = damaged_actorid.toActor(),
                    amount = amount,
                    weaponModel = WeaponModel[weaponid],
                    bodyPart = BodyPart[bodypart]
            )
        }
        return true
    }

    override fun onPlayerClickMap(playerid: Int, fX: Float, fY: Float, fZ: Float): Boolean {
        val result = tryAndCatch {
            onPlayerClickMapHandler.onPlayerClickMap(playerid.toPlayer(), vector3DOf(x = fX, y = fY, z = fZ))
        } ?: OnPlayerClickMapListener.Result.Continue
        return result.value
    }

    override fun onPlayerClickTextDraw(playerid: Int, clickedid: Int): Boolean {
        return tryAndCatch {
            val player = playerid.toPlayer()
            val textDraw = clickedid.toTextDrawOrNull()
            if (textDraw != null) {
                onPlayerClickTextDrawHandler.onPlayerClickTextDraw(player, textDraw).value
            } else {
                onPlayerCancelTextDrawSelectionHandler.onPlayerCancelTextDrawSelection(player).value
            }
        } ?: OnPlayerClickTextDrawListener.Result.NotFound.value
    }

    override fun onPlayerClickPlayerTextDraw(playerid: Int, playertextid: Int): Boolean {
        val result = tryAndCatch {
            onPlayerClickPlayerTextDrawHandler.onPlayerClickPlayerTextDraw(playertextid.toPlayerTextDraw(playerid.toPlayer()))
        } ?: OnPlayerClickPlayerTextDrawListener.Result.NotFound
        return result.value
    }

    override fun onIncomingConnection(playerid: Int, ip_address: String, port: Int): Boolean {
        tryAndCatch {
            onIncomingConnectionHandler.onIncomingConnection(PlayerId.valueOf(playerid), ip_address, port)
        }
        return true
    }

    override fun onTrailerUpdate(playerid: Int, vehicleid: Int): Boolean {
        val result = tryAndCatch {
            onTrailerUpdateHandler.onTrailerUpdate(playerid.toPlayer(), vehicleid.toVehicle())
        } ?: OnTrailerUpdateListener.Result.Sync
        return result.value
    }

    override fun onVehicleSirenStateChange(playerid: Int, vehicleid: Int, newstate: Int): Boolean {
        tryAndCatch {
            onVehicleSirenStateChangeHandler.onVehicleSirenStateChange(
                    playerid.toPlayer(),
                    vehicleid.toVehicle(),
                    VehicleSirenState[newstate]
            )
        }
        return true
    }

    override fun onPlayerClickPlayer(playerid: Int, clickedplayerid: Int, source: Int): Boolean {
        val result = tryAndCatch {
            onPlayerClickPlayerHandler.onPlayerClickPlayer(
                    player = playerid.toPlayer(),
                    clickedPlayer = clickedplayerid.toPlayer(),
                    source = ClickPlayerSource[source]
            )
        } ?: OnPlayerClickPlayerListener.Result.Continue
        return result.value
    }

    override fun onPlayerEditObject(
            playerid: Int,
            playerobject: Boolean,
            objectid: Int,
            response: Int,
            fX: Float,
            fY: Float,
            fZ: Float,
            fRotX: Float,
            fRotY: Float,
            fRotZ: Float
    ): Boolean {
        tryAndCatch {
            if (playerobject) {
                onPlayerEditPlayerMapObjectHandler.onPlayerEditPlayerMapObject(
                        playerMapObject = objectid.toPlayerMapObject(playerid.toPlayer()),
                        response = ObjectEditResponse[response],
                        offset = vector3DOf(x = fX, y = fY, z = fZ),
                        rotation = vector3DOf(x = fRotX, y = fRotY, z = fRotZ)
                )
            } else {
                onPlayerEditMapObjectHandler.onPlayerEditMapObject(
                        player = playerid.toPlayer(),
                        mapObject = objectid.toMapObject(),
                        response = ObjectEditResponse[response],
                        offset = vector3DOf(x = fX, y = fY, z = fZ),
                        rotation = vector3DOf(x = fRotX, y = fRotY, z = fRotZ)
                )
            }
        }
        return true
    }

    override fun onPlayerEditAttachedObject(
            playerid: Int,
            response: Int,
            index: Int,
            modelid: Int,
            boneid: Int,
            fOffsetX: Float,
            fOffsetY: Float,
            fOffsetZ: Float,
            fRotX: Float,
            fRotY: Float,
            fRotZ: Float,
            fScaleX: Float,
            fScaleY: Float,
            fScaleZ: Float
    ): Boolean {
        tryAndCatch {
            val player = playerid.toPlayer()
            onPlayerEditAttachedObjectHandler.onPlayerEditAttachedObject(
                    player = player,
                    slot = player.attachedObjectSlots[index],
                    response = AttachedObjectEditResponse[response],
                    modelId = modelid,
                    bone = Bone[boneid],
                    offset = vector3DOf(x = fOffsetX, y = fOffsetY, z = fOffsetZ),
                    rotation = vector3DOf(x = fRotX, y = fRotY, z = fRotZ),
                    scale = vector3DOf(x = fScaleX, y = fScaleY, z = fScaleZ)
            )
        }
        return true
    }

    override fun onPlayerSelectObject(
            playerid: Int,
            type: Int,
            objectid: Int,
            modelid: Int,
            fX: Float,
            fY: Float,
            fZ: Float
    ): Boolean {
        tryAndCatch {
            when (type) {
                SAMPConstants.SELECT_OBJECT_GLOBAL_OBJECT -> {
                    onPlayerSelectMapObjectHandler.onPlayerSelectMapObject(
                            playerid.toPlayer(),
                            objectid.toMapObject(),
                            modelid,
                            vector3DOf(x = fX, y = fY, z = fZ)
                    )
                }
                SAMPConstants.SELECT_OBJECT_PLAYER_OBJECT -> {
                    onPlayerSelectPlayerMapObjectHandler.onPlayerSelectPlayerMapObject(
                            objectid.toPlayerMapObject(playerid.toPlayer()),
                            modelid,
                            vector3DOf(x = fX, y = fY, z = fZ)
                    )
                }
                else -> throw IllegalArgumentException("Invalid OnPlayerSelectObject type: $type")
            }
        }
        return true
    }

    override fun onPlayerWeaponShot(
            playerid: Int,
            weaponid: Int,
            hittype: Int,
            hitid: Int,
            fX: Float,
            fY: Float,
            fZ: Float
    ): Boolean {
        val result = tryAndCatch {
            val player = playerid.toPlayer()
            val target = when (BulletHitType[hittype]) {
                BulletHitType.NONE -> NoHitTarget
                BulletHitType.PLAYER -> PlayerHitTarget(hitid.toPlayer())
                BulletHitType.VEHICLE -> VehicleHitTarget(hitid.toVehicle())
                BulletHitType.OBJECT -> MapObjectHitTarget(hitid.toMapObject())
                BulletHitType.PLAYER_OBJECT -> PlayerMapObjectHitTarget(
                        hitid.toPlayerMapObject(player)
                )
            }
            onPlayerWeaponShotHandler.onPlayerShotWeapon(
                    player,
                    WeaponModel[weaponid],
                    target,
                    vector3DOf(x = fX, y = fY, z = fZ)
            )
        } ?: OnPlayerWeaponShotListener.Result.AllowDamage
        return result.value
    }

    override fun onPlayerRequestDownload(playerid: Int, type: Int, crc: Int): Boolean {
        tryAndCatch {
            onPlayerRequestDownloadHandler.onPlayerRequestDownload(playerid.toPlayer(), DownloadRequestType[type], crc)
        }
        return true
    }

}