package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCancelTextDrawSelectionListener
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
import ch.leadrian.samp.kamp.core.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.MenuRow
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.Server
import ch.leadrian.samp.kamp.core.runtime.amx.AmxCallbackExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.EntityResolver
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerFactory
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class CallbackProcessorTest {

    private lateinit var callbackProcessor: CallbackProcessor

    private val server: Server = mockk()
    private val playerFactory: PlayerFactory = mockk()
    private val onActorStreamInHandler: OnActorStreamInHandler = mockk()
    private val onActorStreamOutHandler: OnActorStreamOutHandler = mockk()
    private val onDialogResponseHandler: OnDialogResponseHandler = mockk()
    private val onEnterExitModShopHandler: OnEnterExitModShopHandler = mockk()
    private val onGameModeExitHandler: OnGameModeExitHandler = mockk()
    private val onGameModeInitHandler: OnGameModeInitHandler = mockk()
    private val onIncomingConnectionHandler: OnIncomingConnectionHandler = mockk()
    private val onMapObjectMovedHandler: OnMapObjectMovedHandler = mockk()
    private val onPlayerClickMapHandler: OnPlayerClickMapHandler = mockk()
    private val onPlayerClickPlayerHandler: OnPlayerClickPlayerHandler = mockk()
    private val onPlayerClickPlayerTextDrawHandler: OnPlayerClickPlayerTextDrawHandler = mockk()
    private val onPlayerClickTextDrawHandler: OnPlayerClickTextDrawHandler = mockk()
    private val onPlayerCancelTextDrawSelectionHandler: OnPlayerCancelTextDrawSelectionHandler = mockk()
    private val onPlayerCommandTextHandler: OnPlayerCommandTextHandler = mockk()
    private val onPlayerConnectHandler: OnPlayerConnectHandler = mockk()
    private val onPlayerDeathHandler: OnPlayerDeathHandler = mockk()
    private val onPlayerDisconnectHandler: OnPlayerDisconnectHandler = mockk()
    private val onPlayerEditAttachedObjectHandler: OnPlayerEditAttachedObjectHandler = mockk()
    private val onPlayerEditMapObjectHandler: OnPlayerEditMapObjectHandler = mockk()
    private val onPlayerEditPlayerMapObjectHandler: OnPlayerEditPlayerMapObjectHandler = mockk()
    private val onPlayerEnterCheckpointHandler: OnPlayerEnterCheckpointHandler = mockk()
    private val onPlayerEnterRaceCheckpointHandler: OnPlayerEnterRaceCheckpointHandler = mockk()
    private val onPlayerEnterVehicleHandler: OnPlayerEnterVehicleHandler = mockk()
    private val onPlayerExitedMenuHandler: OnPlayerExitedMenuHandler = mockk()
    private val onPlayerExitVehicleHandler: OnPlayerExitVehicleHandler = mockk()
    private val onPlayerGiveDamageActorHandler: OnPlayerGiveDamageActorHandler = mockk()
    private val onPlayerGiveDamageHandler: OnPlayerGiveDamageHandler = mockk()
    private val onPlayerInteriorChangeHandler: OnPlayerInteriorChangeHandler = mockk()
    private val onPlayerKeyStateChangeHandler: OnPlayerKeyStateChangeHandler = mockk()
    private val onPlayerLeaveCheckpointHandler: OnPlayerLeaveCheckpointHandler = mockk()
    private val onPlayerLeaveRaceCheckpointHandler: OnPlayerLeaveRaceCheckpointHandler = mockk()
    private val onPlayerMapObjectMovedHandler: OnPlayerMapObjectMovedHandler = mockk()
    private val onPlayerPickUpPickupHandler: OnPlayerPickUpPickupHandler = mockk()
    private val onPlayerRequestClassHandler: OnPlayerRequestClassHandler = mockk()
    private val onPlayerRequestSpawnHandler: OnPlayerRequestSpawnHandler = mockk()
    private val onPlayerSelectedMenuRowHandler: OnPlayerSelectedMenuRowHandler = mockk()
    private val onPlayerSelectMapObjectHandler: OnPlayerSelectMapObjectHandler = mockk()
    private val onPlayerSelectPlayerMapObjectHandler: OnPlayerSelectPlayerMapObjectHandler = mockk()
    private val onPlayerSpawnHandler: OnPlayerSpawnHandler = mockk()
    private val onPlayerStateChangeHandler: OnPlayerStateChangeHandler = mockk()
    private val onPlayerStreamInHandler: OnPlayerStreamInHandler = mockk()
    private val onPlayerStreamOutHandler: OnPlayerStreamOutHandler = mockk()
    private val onPlayerTakeDamageHandler: OnPlayerTakeDamageHandler = mockk()
    private val onPlayerTextHandler: OnPlayerTextHandler = mockk()
    private val onPlayerUpdateHandler: OnPlayerUpdateHandler = mockk()
    private val onPlayerWeaponShotHandler: OnPlayerWeaponShotHandler = mockk()
    private val onProcessTickHandler: OnProcessTickHandler = mockk()
    private val onRconCommandHandler: OnRconCommandHandler = mockk()
    private val onRconLoginAttemptHandler: OnRconLoginAttemptHandler = mockk()
    private val onTrailerUpdateHandler: OnTrailerUpdateHandler = mockk()
    private val onUnoccupiedVehicleUpdateHandler: OnUnoccupiedVehicleUpdateHandler = mockk()
    private val onVehicleDamageStatusUpdateHandler: OnVehicleDamageStatusUpdateHandler = mockk()
    private val onVehicleDeathHandler: OnVehicleDeathHandler = mockk()
    private val onVehicleModHandler: OnVehicleModHandler = mockk()
    private val onVehiclePaintjobHandler: OnVehiclePaintjobHandler = mockk()
    private val onVehicleResprayHandler: OnVehicleResprayHandler = mockk()
    private val onVehicleSirenStateChangeHandler: OnVehicleSirenStateChangeHandler = mockk()
    private val onVehicleSpawnHandler: OnVehicleSpawnHandler = mockk()
    private val onVehicleStreamInHandler: OnVehicleStreamInHandler = mockk()
    private val onVehicleStreamOutHandler: OnVehicleStreamOutHandler = mockk()
    private val onPlayerRequestDownloadHandler: OnPlayerRequestDownloadHandler = mockk()
    private val amxCallbackExecutor: AmxCallbackExecutor = mockk()
    private val entityResolver: EntityResolver = mockk()

    @BeforeEach
    fun setUp() {
        callbackProcessor = CallbackProcessor(
                server,
                playerFactory,
                onActorStreamInHandler,
                onActorStreamOutHandler,
                onDialogResponseHandler,
                onEnterExitModShopHandler,
                onGameModeExitHandler,
                onGameModeInitHandler,
                onIncomingConnectionHandler,
                onMapObjectMovedHandler,
                onPlayerClickMapHandler,
                onPlayerClickPlayerHandler,
                onPlayerClickPlayerTextDrawHandler,
                onPlayerClickTextDrawHandler,
                onPlayerCancelTextDrawSelectionHandler,
                onPlayerCommandTextHandler,
                onPlayerConnectHandler,
                onPlayerDeathHandler,
                onPlayerDisconnectHandler,
                onPlayerEditAttachedObjectHandler,
                onPlayerEditMapObjectHandler,
                onPlayerEditPlayerMapObjectHandler,
                onPlayerEnterCheckpointHandler,
                onPlayerEnterRaceCheckpointHandler,
                onPlayerEnterVehicleHandler,
                onPlayerExitedMenuHandler,
                onPlayerExitVehicleHandler,
                onPlayerGiveDamageActorHandler,
                onPlayerGiveDamageHandler,
                onPlayerInteriorChangeHandler,
                onPlayerKeyStateChangeHandler,
                onPlayerLeaveCheckpointHandler,
                onPlayerLeaveRaceCheckpointHandler,
                onPlayerMapObjectMovedHandler,
                onPlayerPickUpPickupHandler,
                onPlayerRequestClassHandler,
                onPlayerRequestSpawnHandler,
                onPlayerSelectedMenuRowHandler,
                onPlayerSelectMapObjectHandler,
                onPlayerSelectPlayerMapObjectHandler,
                onPlayerSpawnHandler,
                onPlayerStateChangeHandler,
                onPlayerStreamInHandler,
                onPlayerStreamOutHandler,
                onPlayerTakeDamageHandler,
                onPlayerTextHandler,
                onPlayerUpdateHandler,
                onPlayerWeaponShotHandler,
                onProcessTickHandler,
                onRconCommandHandler,
                onRconLoginAttemptHandler,
                onTrailerUpdateHandler,
                onUnoccupiedVehicleUpdateHandler,
                onVehicleDamageStatusUpdateHandler,
                onVehicleDeathHandler,
                onVehicleModHandler,
                onVehiclePaintjobHandler,
                onVehicleResprayHandler,
                onVehicleSirenStateChangeHandler,
                onVehicleSpawnHandler,
                onVehicleStreamInHandler,
                onVehicleStreamOutHandler,
                onPlayerRequestDownloadHandler,
                amxCallbackExecutor,
                entityResolver
        )
    }

    @Test
    fun shouldCallOnProcessTick() {
        every { onProcessTickHandler.onProcessTick() } just Runs

        callbackProcessor.onProcessTick()

        verify { onProcessTickHandler.onProcessTick() }
    }

    @Test
    fun shouldCallOnPublicCall() {
        val heapPointer = 1234
        val expectedResult = 69
        val paramsAddress = 1337
        val callbackName = "onTest"
        every {
            amxCallbackExecutor.onPublicCall(
                    name = callbackName,
                    paramsAddress = paramsAddress,
                    heapPointer = heapPointer
            )
        } returns expectedResult

        val result = callbackProcessor.onPublicCall(
                name = callbackName,
                paramsAddress = paramsAddress,
                heapPointer = heapPointer
        )

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Test
    fun shouldCallOnGameModeInit() {
        every { onGameModeInitHandler.onGameModeInit() } just Runs

        callbackProcessor.onGameModeInit()

        verify { onGameModeInitHandler.onGameModeInit() }
    }

    @Nested
    inner class OnGameModeExitTests {

        @BeforeEach
        fun setUp() {
            every { onGameModeExitHandler.onGameModeExit() } just Runs
            every { server.stop() } just Runs
        }

        @Test
        fun shouldCallOnGameModeExit() {
            callbackProcessor.onGameModeExit()

            verify { onGameModeExitHandler.onGameModeExit() }
        }

        @Test
        fun shouldStopServer() {
            callbackProcessor.onGameModeExit()

            verify { server.stop() }
        }

    }

    @Test
    fun shouldCallOnPlayerConnect() {
        val playerId = 69
        val player = mockk<Player>()
        every { onPlayerConnectHandler.onPlayerConnect(any()) } just Runs
        every { playerFactory.create(PlayerId.valueOf(playerId)) } returns player

        callbackProcessor.onPlayerConnect(playerId)

        verify { onPlayerConnectHandler.onPlayerConnect(player) }
    }

    @Nested
    inner class OnPlayerDisconnectTests {

        private val player: Player = mockk()
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            every { player.id } returns PlayerId.valueOf(playerId)
            every { player.onDisconnect() } just Runs
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { onPlayerDisconnectHandler.onPlayerDisconnect(any(), any()) } just Runs
        }

        @ParameterizedTest
        @EnumSource(DisconnectReason::class)
        fun shouldCallOnPlayerDisconnect(reason: DisconnectReason) {
            callbackProcessor.onPlayerDisconnect(playerId, reason.value)

            verify { onPlayerDisconnectHandler.onPlayerDisconnect(player, reason) }
        }

        @Test
        fun shouldDisconnectThePlayer() {
            callbackProcessor.onPlayerDisconnect(playerId, DisconnectReason.QUIT.value)

            verify { player.onDisconnect() }
        }
    }

    @Test
    fun shouldCallOnPlayerSpawn() {
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerSpawnHandler.onPlayerSpawn(any()) } just Runs

        callbackProcessor.onPlayerSpawn(playerId)

        verify { onPlayerSpawnHandler.onPlayerSpawn(player) }
    }

    @Nested
    inner class OnPlayerDeathTests {

        private val player: Player = mockk()
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            every { player.id } returns PlayerId.valueOf(playerId)
            every { onPlayerDeathHandler.onPlayerDeath(any(), any(), any()) } just Runs
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun shouldCallOnPlayerDeath() {
            val killerId = 100
            val killer = mockk<Player>()
            every { entityResolver.run { killerId.toPlayerOrNull() } } returns killer

            callbackProcessor.onPlayerDeath(playerId, killerId, WeaponModel.FLAMETHROWER.value)

            verify { onPlayerDeathHandler.onPlayerDeath(player, killer, WeaponModel.FLAMETHROWER) }
        }

        @Test
        fun givenNoKillerItShouldCallOnPlayerDeath() {
            val killerId = 100
            every { entityResolver.run { killerId.toPlayerOrNull() } } returns null

            callbackProcessor.onPlayerDeath(playerId, killerId, WeaponModel.FLAMETHROWER.value)

            verify { onPlayerDeathHandler.onPlayerDeath(player, null, WeaponModel.FLAMETHROWER) }
        }
    }

    @Test
    fun shouldCallOnVehicleSpawn() {
        val vehicleId = 69
        val vehicle = mockk<Vehicle>()
        every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        every { onVehicleSpawnHandler.onVehicleSpawn(any()) } just Runs

        callbackProcessor.onVehicleSpawn(vehicleId)

        verify { onVehicleSpawnHandler.onVehicleSpawn(vehicle) }
    }

    @Nested
    inner class OnVehicleDeathTests {

        private val vehicle: Vehicle = mockk()
        private val vehicleId = 69

        @BeforeEach
        fun setUp() {
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            every { onVehicleDeathHandler.onVehicleDeath(any(), any()) } just Runs
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        }

        @Test
        fun shouldCallOnVehicleDeath() {
            val killerId = 100
            val killer = mockk<Player>()
            every { entityResolver.run { killerId.toPlayerOrNull() } } returns killer

            callbackProcessor.onVehicleDeath(vehicleId, killerId)

            verify { onVehicleDeathHandler.onVehicleDeath(vehicle, killer) }
        }

        @Test
        fun givenNoKillerItShouldCallOnVehicleDeath() {
            val killerId = 100
            every { entityResolver.run { killerId.toPlayerOrNull() } } returns null

            callbackProcessor.onVehicleDeath(vehicleId, killerId)

            verify { onVehicleDeathHandler.onVehicleDeath(vehicle, null) }
        }
    }

    @Nested
    inner class OnPlayerTextTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun shouldCallOnPlayerText() {
            every {
                onPlayerTextHandler.onPlayerText(any(), any())
            } returns OnPlayerTextListener.Result.Allowed

            val result = callbackProcessor.onPlayerText(playerId, "Hello")

            assertAll(
                    { verify { onPlayerTextHandler.onPlayerText(player, "Hello") } },
                    { assertThat(result).isEqualTo(true) }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnTrue() {
            every {
                onPlayerTextHandler.onPlayerText(any(), any())
            } throws Exception()

            val result = callbackProcessor.onPlayerText(playerId, "Hello")

            assertThat(result)
                    .isTrue()
        }
    }

    @Nested
    inner class OnPlayerCommandTextTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun shouldCallOnPlayerCommandText() {
            every {
                onPlayerCommandTextHandler.onPlayerCommandText(any(), any())
            } returns OnPlayerCommandTextListener.Result.Processed

            val result = callbackProcessor.onPlayerCommandText(playerId, "/hello")

            assertAll(
                    { verify { onPlayerCommandTextHandler.onPlayerCommandText(player, "/hello") } },
                    { assertThat(result).isEqualTo(true) }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnFalse() {
            every {
                onPlayerCommandTextHandler.onPlayerCommandText(any(), any())
            } throws Exception()

            val result = callbackProcessor.onPlayerCommandText(playerId, "/hello")

            assertThat(result)
                    .isFalse()
        }
    }

    @Nested
    inner class OnPlayerRequestClassTests {

        private val playerId = 69
        private val player = mockk<Player>()
        private val playerClassId = 1234
        private val playerClass = mockk<PlayerClass>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { entityResolver.run { playerClassId.toPlayerClass() } } returns playerClass
        }

        @Test
        fun shouldCallOnPlayerRequestClass() {
            every {
                onPlayerRequestClassHandler.onPlayerRequestClass(any(), any())
            } returns OnPlayerRequestClassListener.Result.Allow

            val result = callbackProcessor.onPlayerRequestClass(playerId, playerClassId)

            assertAll(
                    { verify { onPlayerRequestClassHandler.onPlayerRequestClass(player, playerClass) } },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnTrue() {
            every {
                onPlayerRequestClassHandler.onPlayerRequestClass(any(), any())
            } throws Exception()

            val result = callbackProcessor.onPlayerRequestClass(playerId, playerClassId)

            assertThat(result)
                    .isTrue()
        }
    }

    @Test
    fun shouldCallOnPlayerExitVehicle() {
        val playerId = 69
        val player = mockk<Player>()
        val vehicleId = 1234
        val vehicle = mockk<Vehicle>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        every { onPlayerExitVehicleHandler.onPlayerExitVehicle(any(), any()) } just Runs

        callbackProcessor.onPlayerExitVehicle(playerId, vehicleId)

        verify { onPlayerExitVehicleHandler.onPlayerExitVehicle(player, vehicle) }
    }

    @Test
    fun shouldCallOnPlayerStateChange() {
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerStateChangeHandler.onPlayerStateChange(any(), any(), any()) } just Runs

        callbackProcessor.onPlayerStateChange(playerId, PlayerState.ON_FOOT.value, PlayerState.DRIVER.value)

        verify { onPlayerStateChangeHandler.onPlayerStateChange(player, PlayerState.ON_FOOT, PlayerState.DRIVER) }
    }

    @Test
    fun shouldCallOnPlayerEnterCheckpoint() {
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerEnterCheckpointHandler.onPlayerEnterCheckpoint(any()) } just Runs

        callbackProcessor.onPlayerEnterCheckpoint(playerId)

        verify { onPlayerEnterCheckpointHandler.onPlayerEnterCheckpoint(player) }
    }

    @Test
    fun shouldCallOnPlayerLeaveCheckpoint() {
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerLeaveCheckpointHandler.onPlayerLeaveCheckpoint(any()) } just Runs

        callbackProcessor.onPlayerLeaveCheckpoint(playerId)

        verify { onPlayerLeaveCheckpointHandler.onPlayerLeaveCheckpoint(player) }
    }

    @Test
    fun shouldCallOnPlayerEnterRaceCheckpoint() {
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerEnterRaceCheckpointHandler.onPlayerEnterRaceCheckpoint(any()) } just Runs

        callbackProcessor.onPlayerEnterRaceCheckpoint(playerId)

        verify { onPlayerEnterRaceCheckpointHandler.onPlayerEnterRaceCheckpoint(player) }
    }

    @Test
    fun shouldCallOnPlayerLeaveRaceCheckpoint() {
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerLeaveRaceCheckpointHandler.onPlayerLeaveRaceCheckpoint(any()) } just Runs

        callbackProcessor.onPlayerLeaveRaceCheckpoint(playerId)

        verify { onPlayerLeaveRaceCheckpointHandler.onPlayerLeaveRaceCheckpoint(player) }
    }

    @Nested
    inner class OnRconCommandTests {

        @Test
        fun shouldCallOnRconCommand() {
            every {
                onRconCommandHandler.onRconCommand(any())
            } returns OnRconCommandListener.Result.Processed

            val result = callbackProcessor.onRconCommand("/hello")

            assertAll(
                    { verify { onRconCommandHandler.onRconCommand("/hello") } },
                    { assertThat(result).isEqualTo(true) }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnFalse() {
            every {
                onRconCommandHandler.onRconCommand(any())
            } throws Exception()

            val result = callbackProcessor.onRconCommand("/hello")

            assertThat(result)
                    .isFalse()
        }
    }

    @Nested
    inner class OnPlayerRequestSpawnTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun shouldCallOnPlayerRequestSpawn() {
            every {
                onPlayerRequestSpawnHandler.onPlayerRequestSpawn(any())
            } returns OnPlayerRequestSpawnListener.Result.Granted

            val result = callbackProcessor.onPlayerRequestSpawn(playerId)

            assertAll(
                    { verify { onPlayerRequestSpawnHandler.onPlayerRequestSpawn(player) } },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnFalse() {
            every {
                onPlayerRequestSpawnHandler.onPlayerRequestSpawn(any())
            } throws Exception()

            val result = callbackProcessor.onPlayerRequestSpawn(playerId)

            assertThat(result)
                    .isFalse()
        }
    }

    @Test
    fun shouldCallOnMapObjectMoved() {
        val mapObjectId = 69
        val mapObject = mockk<MapObject>()
        every { entityResolver.run { mapObjectId.toMapObject() } } returns mapObject
        every { onMapObjectMovedHandler.onMapObjectMoved(any()) } just Runs

        callbackProcessor.onObjectMoved(mapObjectId)

        verify { onMapObjectMovedHandler.onMapObjectMoved(mapObject) }
    }

    @Test
    fun shouldCallOnPlayerMapObjectMoved() {
        val playerId = 187
        val player = mockk<Player>()
        val playerMapObjectId = 69
        val playerMapObject = mockk<PlayerMapObject>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { playerMapObjectId.toPlayerMapObject(player) } } returns playerMapObject
        every { onPlayerMapObjectMovedHandler.onPlayerMapObjectMoved(any()) } just Runs

        callbackProcessor.onPlayerObjectMoved(playerid = playerId, objectid = playerMapObjectId)

        verify { onPlayerMapObjectMovedHandler.onPlayerMapObjectMoved(playerMapObject) }
    }

    @Test
    fun shouldCallOnPlayerPickUpPickup() {
        val playerId = 69
        val player = mockk<Player>()
        val pickupId = 1234
        val pickup = mockk<Pickup>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { pickupId.toPickup() } } returns pickup
        every { onPlayerPickUpPickupHandler.onPlayerPickUpPickup(any(), any()) } just Runs

        callbackProcessor.onPlayerPickUpPickup(playerId, pickupId)

        verify { onPlayerPickUpPickupHandler.onPlayerPickUpPickup(player, pickup) }
    }

    @Nested
    inner class OnVehicleModTests {

        private val playerId = 69
        private val player = mockk<Player>()
        private val vehicleId = 1234
        private val vehicle = mockk<Vehicle>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        }

        @Test
        fun shouldCallOnVehicleMod() {
            every {
                onVehicleModHandler.onVehicleMod(any(), any(), any())
            } returns OnVehicleModListener.Result.Sync

            val result = callbackProcessor.onVehicleMod(playerId, vehicleId, VehicleComponentModel.EXHAUST_LARGE.value)

            assertAll(
                    {
                        verify {
                            onVehicleModHandler.onVehicleMod(
                                    player,
                                    vehicle,
                                    VehicleComponentModel.EXHAUST_LARGE
                            )
                        }
                    },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnFalse() {
            every {
                onVehicleModHandler.onVehicleMod(any(), any(), any())
            } throws Exception()

            val result = callbackProcessor.onVehicleMod(playerId, vehicleId, VehicleComponentModel.EXHAUST_LARGE.value)

            assertThat(result)
                    .isFalse()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldCallOnEnterExitModShop(entered: Boolean) {
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onEnterExitModShopHandler.onEnterExitModShop(any(), any(), any()) } just Runs

        callbackProcessor.onEnterExitModShop(playerId, entered, 69)

        verify { onEnterExitModShopHandler.onEnterExitModShop(player, entered, 69) }
    }

    @Test
    fun shouldCallOnVehiclePaintjob() {
        val playerId = 69
        val player = mockk<Player>()
        val vehicleId = 1234
        val vehicle = mockk<Vehicle>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        every { onVehiclePaintjobHandler.onVehiclePaintjob(any(), any(), any()) } just Runs

        callbackProcessor.onVehiclePaintjob(playerId, vehicleId, 3)

        verify { onVehiclePaintjobHandler.onVehiclePaintjob(player, vehicle, 3) }
    }

    @Nested
    inner class OnVehicleResprayTests {

        private val playerId = 69
        private val player = mockk<Player>()
        private val vehicleId = 1234
        private val vehicle = mockk<Vehicle>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        }

        @Test
        fun shouldCallOnVehicleRespray() {
            every {
                onVehicleResprayHandler.onVehicleRespray(any(), any(), any())
            } returns OnVehicleResprayListener.Result.Sync

            val result = callbackProcessor.onVehicleRespray(playerId, vehicleId, 3, 6)

            assertAll(
                    {
                        verify {
                            onVehicleResprayHandler.onVehicleRespray(
                                    player,
                                    vehicle,
                                    vehicleColorsOf(3, 6)
                            )
                        }
                    },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnFalse() {
            every {
                onVehicleResprayHandler.onVehicleRespray(any(), any(), any())
            } throws Exception()

            val result = callbackProcessor.onVehicleRespray(
                    playerid = playerId,
                    vehicleid = vehicleId,
                    color1 = 3,
                    color2 = 6
            )

            assertThat(result)
                    .isFalse()
        }
    }

    @Test
    fun shouldCallOnVehicleDamageStatusUpdate() {
        val playerId = 69
        val player = mockk<Player>()
        val vehicleId = 1234
        val vehicle = mockk<Vehicle>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        every { onVehicleDamageStatusUpdateHandler.onVehicleDamageStatusUpdate(any(), any()) } just Runs

        callbackProcessor.onVehicleDamageStatusUpdate(vehicleid = vehicleId, playerid = playerId)

        verify { onVehicleDamageStatusUpdateHandler.onVehicleDamageStatusUpdate(vehicle, player) }
    }

    @Nested
    inner class OnUnoccupiedVehicleUpdateTests {

        private val playerId = 69
        private val player = mockk<Player>()
        private val vehicleId = 1234
        private val vehicle = mockk<Vehicle>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        }

        @ParameterizedTest
        @ValueSource(ints = [1, 2, 3])
        fun shouldCallOnUnoccupiedVehicleUpdate(passengerSeat: Int) {
            every {
                onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns OnUnoccupiedVehicleUpdateListener.Result.Sync

            val result = callbackProcessor.onUnoccupiedVehicleUpdate(
                    vehicleid = vehicleId,
                    playerid = playerId,
                    passenger_seat = passengerSeat,
                    new_x = 1f,
                    new_y = 2f,
                    new_z = 3f,
                    vel_x = 4f,
                    vel_y = 5f,
                    vel_z = 6f
            )

            assertAll(
                    {
                        verify {
                            onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                                    vehicle = vehicle,
                                    player = player,
                                    passengerSeat = passengerSeat,
                                    coordinates = vector3DOf(1f, 2f, 3f),
                                    velocity = vector3DOf(4f, 5f, 6f)
                            )
                        }
                    },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenPassengerSeatItZeroItShouldCallHandlerWithSeatNull() {
            every {
                onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns OnUnoccupiedVehicleUpdateListener.Result.Sync

            val result = callbackProcessor.onUnoccupiedVehicleUpdate(
                    vehicleid = vehicleId,
                    playerid = playerId,
                    passenger_seat = 0,
                    new_x = 1f,
                    new_y = 2f,
                    new_z = 3f,
                    vel_x = 4f,
                    vel_y = 5f,
                    vel_z = 6f
            )

            assertAll(
                    {
                        verify {
                            onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                                    vehicle = vehicle,
                                    player = player,
                                    passengerSeat = null,
                                    coordinates = vector3DOf(1f, 2f, 3f),
                                    velocity = vector3DOf(4f, 5f, 6f)
                            )
                        }
                    },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnTrue() {
            every {
                onUnoccupiedVehicleUpdateHandler.onUnoccupiedVehicleUpdate(
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } throws Exception()

            val result = callbackProcessor.onUnoccupiedVehicleUpdate(
                    vehicleid = vehicleId,
                    playerid = playerId,
                    passenger_seat = 0,
                    new_x = 1f,
                    new_y = 2f,
                    new_z = 3f,
                    vel_x = 4f,
                    vel_y = 5f,
                    vel_z = 6f
            )

            assertThat(result)
                    .isTrue()
        }
    }

    @Nested
    inner class OnPlayerSelectedMenuRowTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { onPlayerSelectedMenuRowHandler.onPlayerSelectedMenuRow(any(), any()) } just Runs
        }

        @Nested
        inner class MenuExistsTests {

            private val menu: Menu = mockk()

            @BeforeEach
            fun setUp() {
                every { player.menu } returns menu
            }

            @Test
            fun givenMenuRowExistsItShouldCallHandler() {
                val menuRow = mockk<MenuRow>()
                every { menu.rows } returns listOf(mockk(), menuRow, mockk())

                callbackProcessor.onPlayerSelectedMenuRow(playerId, 1)

                verify { onPlayerSelectedMenuRowHandler.onPlayerSelectedMenuRow(player, menuRow) }
            }

            @Test
            fun givenMenuRowDoesNotExistItShouldNotCallHandler() {
                every { menu.rows } returns emptyList()

                callbackProcessor.onPlayerSelectedMenuRow(playerId, 3)

                verify { onPlayerSelectedMenuRowHandler wasNot Called }
            }
        }

        @Test
        fun givenNoMenuItShouldDoNothing() {
            callbackProcessor.onPlayerSelectedMenuRow(playerId, 3)

            verify { onPlayerSelectedMenuRowHandler wasNot Called }
        }

    }

    @Nested
    inner class OnPlayerExitedMenuTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { onPlayerExitedMenuHandler.onPlayerExitedMenu(any(), any()) } just Runs
        }

        @Test
        fun givenMenuRowExistsItShouldCallHandler() {
            val menu: Menu = mockk()
            every { player.menu } returns menu

            callbackProcessor.onPlayerExitedMenu(playerId)

            verify { onPlayerExitedMenuHandler.onPlayerExitedMenu(player, menu) }
        }

        @Test
        fun givenNoMenuItShouldDoNothing() {
            callbackProcessor.onPlayerExitedMenu(playerId)

            verify { onPlayerExitedMenuHandler wasNot Called }
        }

    }

    @Test
    fun shouldCallOnPlayerInteriorChange() {
        val newInteriorId = 123
        val oldInteriorId = 456
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerInteriorChangeHandler.onPlayerInteriorChange(any(), any(), any()) } just Runs

        callbackProcessor.onPlayerInteriorChange(
                playerid = playerId,
                newinteriorid = newInteriorId,
                oldinteriorid = oldInteriorId
        )

        verify {
            onPlayerInteriorChangeHandler.onPlayerInteriorChange(
                    player = player,
                    newInteriorId = newInteriorId,
                    oldInteriorId = oldInteriorId
            )
        }
    }

    @Test
    fun shouldCallOnPlayerKeyStateChange() {
        val newKeys = 123
        val oldKeys = 456
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerKeyStateChangeHandler.onPlayerKeyStateChange(any(), any(), any()) } just Runs

        callbackProcessor.onPlayerKeyStateChange(
                playerid = playerId,
                newkeys = newKeys,
                oldkeys = oldKeys
        )

        verify {
            onPlayerKeyStateChangeHandler.onPlayerKeyStateChange(
                    player = player,
                    oldKeys = playerKeysOf(keys = oldKeys),
                    newKeys = playerKeysOf(keys = newKeys)
            )
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldOnRconLoginAttempt(success: Boolean) {
        every { onRconLoginAttemptHandler.onRconLoginAttempt(any(), any(), any()) } just Runs

        callbackProcessor.onRconLoginAttempt(ip = "127.0.0.1", password = "blablabla", success = success)

        verify {
            onRconLoginAttemptHandler.onRconLoginAttempt(
                    ipAddress = "127.0.0.1",
                    password = "blablabla",
                    success = success
            )
        }
    }

    @Nested
    inner class OnPlayerUpdateTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun shouldCallOnPlayerUpdate() {
            every {
                onPlayerUpdateHandler.onPlayerUpdate(any())
            } returns OnPlayerUpdateListener.Result.Sync

            val result = callbackProcessor.onPlayerUpdate(playerId)

            assertAll(
                    { verify { onPlayerUpdateHandler.onPlayerUpdate(player) } },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnTrue() {
            every {
                onPlayerUpdateHandler.onPlayerUpdate(any())
            } throws Exception()

            val result = callbackProcessor.onPlayerUpdate(playerId)

            assertThat(result)
                    .isTrue()
        }
    }

    @Test
    fun shouldCallOnPlayerStreamIn() {
        val playerId = 69
        val player = mockk<Player>()
        val forPlayerId = 1234
        val forPlayer = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { forPlayerId.toPlayer() } } returns forPlayer
        every { onPlayerStreamInHandler.onPlayerStreamIn(any(), any()) } just Runs

        callbackProcessor.onPlayerStreamIn(playerid = playerId, forplayerid = forPlayerId)

        verify { onPlayerStreamInHandler.onPlayerStreamIn(player = player, forPlayer = forPlayer) }
    }

    @Test
    fun shouldCallOnPlayerStreamOut() {
        val playerId = 69
        val player = mockk<Player>()
        val forPlayerId = 1234
        val forPlayer = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { forPlayerId.toPlayer() } } returns forPlayer
        every { onPlayerStreamOutHandler.onPlayerStreamOut(any(), any()) } just Runs

        callbackProcessor.onPlayerStreamOut(playerid = playerId, forplayerid = forPlayerId)

        verify { onPlayerStreamOutHandler.onPlayerStreamOut(player = player, forPlayer = forPlayer) }
    }

    @Test
    fun shouldCallOnVehicleStreamIn() {
        val playerId = 69
        val player = mockk<Player>()
        val vehicleId = 1234
        val vehicle = mockk<Vehicle>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        every { onVehicleStreamInHandler.onVehicleStreamIn(any(), any()) } just Runs

        callbackProcessor.onVehicleStreamIn(vehicleid = vehicleId, forplayerid = playerId)

        verify { onVehicleStreamInHandler.onVehicleStreamIn(vehicle, player) }
    }

    @Test
    fun shouldCallOnVehicleStreamOut() {
        val playerId = 69
        val player = mockk<Player>()
        val vehicleId = 1234
        val vehicle = mockk<Vehicle>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        every { onVehicleStreamOutHandler.onVehicleStreamOut(any(), any()) } just Runs

        callbackProcessor.onVehicleStreamOut(vehicleid = vehicleId, forplayerid = playerId)

        verify { onVehicleStreamOutHandler.onVehicleStreamOut(vehicle, player) }
    }

    @Test
    fun shouldCallOnActorStreamIn() {
        val playerId = 69
        val player = mockk<Player>()
        val actorId = 1234
        val actor = mockk<Actor>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { actorId.toActor() } } returns actor
        every { onActorStreamInHandler.onActorStreamIn(any(), any()) } just Runs

        callbackProcessor.onActorStreamIn(actorid = actorId, forplayerid = playerId)

        verify { onActorStreamInHandler.onActorStreamIn(actor, player) }
    }

    @Test
    fun shouldCallOnActorStreamOut() {
        val playerId = 69
        val player = mockk<Player>()
        val actorId = 1234
        val actor = mockk<Actor>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { actorId.toActor() } } returns actor
        every { onActorStreamOutHandler.onActorStreamOut(any(), any()) } just Runs

        callbackProcessor.onActorStreamOut(actorid = actorId, forplayerid = playerId)

        verify { onActorStreamOutHandler.onActorStreamOut(actor, player) }
    }

    @Nested
    inner class OnDialogResponseTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun shouldCallOnDialogResponse() {
            val dialogId = 1337
            val response = DialogResponse.LEFT_BUTTON
            val listItem = 87
            every {
                onDialogResponseHandler.onDialogResponse(any(), any(), any(), any(), any())
            } returns OnDialogResponseListener.Result.Processed

            val result = callbackProcessor.onDialogResponse(
                    playerid = playerId,
                    dialogid = dialogId,
                    response = response.value,
                    listitem = listItem,
                    inputtext = "Hello"
            )

            assertAll(
                    {
                        verify {
                            onDialogResponseHandler.onDialogResponse(
                                    player = player,
                                    dialogId = DialogId.valueOf(dialogId),
                                    response = response,
                                    listItem = listItem,
                                    inputText = "Hello"
                            )
                        }
                    },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnFalse() {
            every {
                onDialogResponseHandler.onDialogResponse(any(), any(), any(), any(), any())
            } throws Exception()

            val result = callbackProcessor.onDialogResponse(
                    playerid = playerId,
                    dialogid = 1234,
                    response = DialogResponse.LEFT_BUTTON.value,
                    listitem = 13,
                    inputtext = "Hello"
            )

            assertThat(result)
                    .isFalse()
        }
    }

    @Nested
    inner class OnPlayerTakeDamageTests {

        private val player: Player = mockk()
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            every { player.id } returns PlayerId.valueOf(playerId)
            every { onPlayerTakeDamageHandler.onPlayerTakeDamage(any(), any(), any(), any(), any()) } just Runs
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun shouldCallOnPlayerTakeDamage() {
            val issuerId = 100
            val issuer = mockk<Player>()
            every { entityResolver.run { issuerId.toPlayerOrNull() } } returns issuer

            callbackProcessor.onPlayerTakeDamage(
                    playerid = playerId,
                    issuerid = issuerId,
                    amount = 13.37f,
                    weaponid = WeaponModel.FLAMETHROWER.value,
                    bodypart = BodyPart.GROIN.value
            )

            verify {
                onPlayerTakeDamageHandler.onPlayerTakeDamage(
                        player = player,
                        issuer = issuer,
                        amount = 13.37f,
                        weaponModel = WeaponModel.FLAMETHROWER,
                        bodyPart = BodyPart.GROIN
                )
            }
        }

        @Test
        fun givenNoIssuerItShouldCallOnPlayerTakeDamage() {
            val issuerId = 100
            every { entityResolver.run { issuerId.toPlayerOrNull() } } returns null

            callbackProcessor.onPlayerTakeDamage(
                    playerid = playerId,
                    issuerid = issuerId,
                    amount = 13.37f,
                    weaponid = WeaponModel.FLAMETHROWER.value,
                    bodypart = BodyPart.GROIN.value
            )

            verify {
                onPlayerTakeDamageHandler.onPlayerTakeDamage(
                        player = player,
                        issuer = null,
                        amount = 13.37f,
                        weaponModel = WeaponModel.FLAMETHROWER,
                        bodyPart = BodyPart.GROIN
                )
            }
        }
    }

    @Test
    fun shouldCallOnPlayerGiveDamage() {
        val player: Player = mockk()
        val playerId = 69
        val issuerId = 100
        val issuer = mockk<Player>()
        every { onPlayerGiveDamageHandler.onPlayerGiveDamage(any(), any(), any(), any(), any()) } just Runs
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { issuerId.toPlayer() } } returns issuer

        callbackProcessor.onPlayerGiveDamage(
                playerid = playerId,
                damagedid = issuerId,
                amount = 13.37f,
                weaponid = WeaponModel.FLAMETHROWER.value,
                bodypart = BodyPart.GROIN.value
        )

        verify {
            onPlayerGiveDamageHandler.onPlayerGiveDamage(
                    player = player,
                    damagedPlayer = issuer,
                    amount = 13.37f,
                    weaponModel = WeaponModel.FLAMETHROWER,
                    bodyPart = BodyPart.GROIN
            )
        }
    }

    @Test
    fun shouldCallOnPlayerGiveDamageActor() {
        val player: Player = mockk()
        val playerId = 69
        val actorId = 100
        val actor = mockk<Actor>()
        every { onPlayerGiveDamageActorHandler.onPlayerGiveDamageActor(any(), any(), any(), any(), any()) } just Runs
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { actorId.toActor() } } returns actor

        callbackProcessor.onPlayerGiveDamageActor(
                playerid = playerId,
                damaged_actorid = actorId,
                amount = 13.37f,
                weaponid = WeaponModel.FLAMETHROWER.value,
                bodypart = BodyPart.GROIN.value
        )

        verify {
            onPlayerGiveDamageActorHandler.onPlayerGiveDamageActor(
                    player = player,
                    actor = actor,
                    amount = 13.37f,
                    weaponModel = WeaponModel.FLAMETHROWER,
                    bodyPart = BodyPart.GROIN
            )
        }
    }

    @Nested
    inner class OnPlayerClickMapTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun shouldCallOnPlayerClickMap() {
            every {
                onPlayerClickMapHandler.onPlayerClickMap(any(), any())
            } returns OnPlayerClickMapListener.Result.Processed

            val result = callbackProcessor.onPlayerClickMap(playerId, 1f, 2f, 3f)

            assertAll(
                    { verify { onPlayerClickMapHandler.onPlayerClickMap(player, vector3DOf(1f, 2f, 3f)) } },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnFalse() {
            every {
                onPlayerClickMapHandler.onPlayerClickMap(any(), any())
            } throws Exception()

            val result = callbackProcessor.onPlayerClickMap(playerId, 1f, 2f, 3f)

            assertThat(result)
                    .isFalse()
        }
    }

    @Nested
    inner class OnPlayerClickTextDrawTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun givenValidTextDrawIdItShouldCallOnPlayerClickTextDrawHandler() {
            val textDrawId = 100
            val textDraw = mockk<TextDraw>()
            every {
                onPlayerClickTextDrawHandler.onPlayerClickTextDraw(any(), any())
            } returns OnPlayerClickTextDrawListener.Result.Processed
            every { entityResolver.run { textDrawId.toTextDrawOrNull() } } returns textDraw

            callbackProcessor.onPlayerClickTextDraw(playerid = playerId, clickedid = textDrawId)

            verify { onPlayerClickTextDrawHandler.onPlayerClickTextDraw(player, textDraw) }
        }

        @Test
        fun givenOnPlayerClickTextDrawHandlerThrowsExceptionItShouldReturnFalse() {
            val textDrawId = 100
            val textDraw = mockk<TextDraw>()
            every {
                onPlayerClickTextDrawHandler.onPlayerClickTextDraw(any(), any())
            } throws Exception()
            every { entityResolver.run { textDrawId.toTextDrawOrNull() } } returns textDraw

            val result = callbackProcessor.onPlayerClickTextDraw(playerid = playerId, clickedid = textDrawId)

            assertThat(result)
                    .isFalse()
        }

        @Test
        fun givenInvalidTextDrawIdItShouldCallOnPlayerCancelTextDrawSelectionHandler() {
            val textDrawId = 100
            every {
                onPlayerCancelTextDrawSelectionHandler.onPlayerCancelTextDrawSelection(any())
            } returns OnPlayerCancelTextDrawSelectionListener.Result.Processed
            every { entityResolver.run { textDrawId.toTextDrawOrNull() } } returns null

            callbackProcessor.onPlayerClickTextDraw(playerid = playerId, clickedid = textDrawId)

            verify { onPlayerCancelTextDrawSelectionHandler.onPlayerCancelTextDrawSelection(player) }
        }

        @Test
        fun givenOnPlayerCancelTextDrawSelectionThrowsExceptionItShouldReturnFalse() {
            val textDrawId = 100
            every {
                onPlayerCancelTextDrawSelectionHandler.onPlayerCancelTextDrawSelection(any())
            } throws Exception()
            every { entityResolver.run { textDrawId.toTextDrawOrNull() } } returns null

            val result = callbackProcessor.onPlayerClickTextDraw(playerid = playerId, clickedid = textDrawId)

            assertThat(result)
                    .isFalse()
        }
    }

    @Nested
    inner class OnPlayerClickPlayerTextDrawTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun givenValidPlayerTextDrawIdItShouldCallOnPlayerClickPlayerTextDrawHandler() {
            val playerTextDrawId = 100
            val playerTextDraw = mockk<PlayerTextDraw>()
            every {
                onPlayerClickPlayerTextDrawHandler.onPlayerClickPlayerTextDraw(any())
            } returns OnPlayerClickPlayerTextDrawListener.Result.Processed
            every { entityResolver.run { playerTextDrawId.toPlayerTextDraw(player) } } returns playerTextDraw

            callbackProcessor.onPlayerClickPlayerTextDraw(playerid = playerId, playertextid = playerTextDrawId)

            verify { onPlayerClickPlayerTextDrawHandler.onPlayerClickPlayerTextDraw(playerTextDraw) }
        }

        @Test
        fun givenOnPlayerClickPlayerTextDrawHandlerThrowsExceptionItShouldReturnFalse() {
            val playerTextDrawId = 100
            val playerTextDraw = mockk<PlayerTextDraw>()
            every {
                onPlayerClickPlayerTextDrawHandler.onPlayerClickPlayerTextDraw(any())
            } throws Exception()
            every { entityResolver.run { playerTextDrawId.toPlayerTextDraw(player) } } returns playerTextDraw

            val result = callbackProcessor.onPlayerClickPlayerTextDraw(
                    playerid = playerId,
                    playertextid = playerTextDrawId
            )

            assertThat(result)
                    .isFalse()
        }
    }

    @Test
    fun shouldCallOnIncomingConnectionHandler() {
        val playerId = 69
        val port = 7777
        every { onIncomingConnectionHandler.onIncomingConnection(any(), any(), any()) } just Runs

        callbackProcessor.onIncomingConnection(playerid = playerId, ip_address = "127.0.0.1", port = port)

        verify { onIncomingConnectionHandler.onIncomingConnection(PlayerId.valueOf(playerId), "127.0.0.1", port) }
    }

    @Nested
    inner class OnTrailerUpdateTests {

        private val playerId = 69
        private val player = mockk<Player>()
        private val vehicleId = 1234
        private val vehicle = mockk<Vehicle>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        }

        @Test
        fun shouldCallOnTrailerUpdate() {
            every {
                onTrailerUpdateHandler.onTrailerUpdate(any(), any())
            } returns OnTrailerUpdateListener.Result.Sync

            val result = callbackProcessor.onTrailerUpdate(playerId, vehicleId)

            assertAll(
                    { verify { onTrailerUpdateHandler.onTrailerUpdate(player, vehicle) } },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnTrue() {
            every {
                onTrailerUpdateHandler.onTrailerUpdate(any(), any())
            } throws Exception()

            val result = callbackProcessor.onTrailerUpdate(playerid = playerId, vehicleid = vehicleId)

            assertThat(result)
                    .isTrue()
        }
    }

    @Test
    fun shouldCallOnVehicleSirenStateChange() {
        val playerId = 69
        val player = mockk<Player>()
        val vehicleId = 1234
        val vehicle = mockk<Vehicle>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
        every {
            onVehicleSirenStateChangeHandler.onVehicleSirenStateChange(any(), any(), any())
        } just Runs

        callbackProcessor.onVehicleSirenStateChange(
                vehicleid = vehicleId,
                playerid = playerId,
                newstate = 1
        )

        verify {
            onVehicleSirenStateChangeHandler.onVehicleSirenStateChange(
                    vehicle = vehicle,
                    player = player,
                    newState = VehicleSirenState[1]
            )
        }
    }

    @Nested
    inner class OnPlayerClickPlayerTests {

        private val playerId = 69
        private val player = mockk<Player>()
        private val clickedPlayerId = 1234
        private val clickedPlayer = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
            every { entityResolver.run { clickedPlayerId.toPlayer() } } returns clickedPlayer
        }

        @Test
        fun shouldCallOnPlayerClickPlayer() {
            every {
                onPlayerClickPlayerHandler.onPlayerClickPlayer(any(), any(), any())
            } returns OnPlayerClickPlayerListener.Result.Processed

            val result = callbackProcessor.onPlayerClickPlayer(
                    playerid = playerId,
                    clickedplayerid = clickedPlayerId,
                    source = ClickPlayerSource.SCOREBOARD.value
            )

            assertAll(
                    {
                        verify {
                            onPlayerClickPlayerHandler.onPlayerClickPlayer(
                                    player = player,
                                    clickedPlayer = clickedPlayer,
                                    source = ClickPlayerSource.SCOREBOARD
                            )
                        }
                    },
                    { assertThat(result).isTrue() }
            )
        }

        @Test
        fun givenHandlerThrowsExceptionItShouldReturnFalse() {
            every {
                onPlayerClickPlayerHandler.onPlayerClickPlayer(any(), any(), any())
            } throws Exception()

            val result = callbackProcessor.onPlayerClickPlayer(
                    playerid = playerId,
                    clickedplayerid = playerId,
                    source = ClickPlayerSource.SCOREBOARD.value
            )

            assertThat(result)
                    .isFalse()
        }
    }

    @Nested
    inner class OnPlayerEditObjectTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun givenGlobalObjectItShouldCallOnPlayerEditMapObjectHandler() {
            val mapObjectId = 123
            val mapObject = mockk<MapObject>()
            every { entityResolver.run { mapObjectId.toMapObject() } } returns mapObject
            every { onPlayerEditMapObjectHandler.onPlayerEditMapObject(any(), any(), any(), any(), any()) } just Runs

            callbackProcessor.onPlayerEditObject(
                    playerid = playerId,
                    playerobject = false,
                    objectid = mapObjectId,
                    response = ObjectEditResponse.FINAL.value,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f,
                    fRotX = 4f,
                    fRotY = 5f,
                    fRotZ = 6f
            )

            verify {
                onPlayerEditMapObjectHandler.onPlayerEditMapObject(
                        player = player,
                        mapObject = mapObject,
                        response = ObjectEditResponse.FINAL,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun givenGlobalObjectItShouldCallOnPlayerEditPlayerMapObjectHandler() {
            val playerMapObjectId = 123
            val playerMapObject = mockk<PlayerMapObject>()
            every { entityResolver.run { playerMapObjectId.toPlayerMapObject(player) } } returns playerMapObject
            every {
                onPlayerEditPlayerMapObjectHandler.onPlayerEditPlayerMapObject(
                        any(),
                        any(),
                        any(),
                        any()
                )
            } just Runs

            callbackProcessor.onPlayerEditObject(
                    playerid = playerId,
                    playerobject = true,
                    objectid = playerMapObjectId,
                    response = ObjectEditResponse.FINAL.value,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f,
                    fRotX = 4f,
                    fRotY = 5f,
                    fRotZ = 6f
            )

            verify {
                onPlayerEditPlayerMapObjectHandler.onPlayerEditPlayerMapObject(
                        playerMapObject = playerMapObject,
                        response = ObjectEditResponse.FINAL,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f)
                )
            }
        }
    }

    @Test
    fun shouldCallOnPlayerEditAttachedObjectHandler() {
        val playerId = 69
        val attachedObjectSlot = mockk<AttachedObjectSlot>()
        val player = mockk<Player> {
            every { attachedObjectSlots[2] } returns attachedObjectSlot
        }
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every {
            onPlayerEditAttachedObjectHandler.onPlayerEditAttachedObject(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
            )
        } just Runs

        callbackProcessor.onPlayerEditAttachedObject(
                playerid = playerId,
                response = AttachedObjectEditResponse.SAVE.value,
                index = 2,
                modelid = 34000,
                boneid = Bone.HEAD.value,
                fOffsetX = 1f,
                fOffsetY = 2f,
                fOffsetZ = 3f,
                fRotX = 4f,
                fRotY = 5f,
                fRotZ = 6f,
                fScaleX = 7f,
                fScaleY = 8f,
                fScaleZ = 9f
        )

        verify {
            onPlayerEditAttachedObjectHandler.onPlayerEditAttachedObject(
                    player = player,
                    slot = attachedObjectSlot,
                    response = AttachedObjectEditResponse.SAVE,
                    modelId = 34000,
                    bone = Bone.HEAD,
                    offset = vector3DOf(1f, 2f, 3f),
                    rotation = vector3DOf(4f, 5f, 6f),
                    scale = vector3DOf(7f, 8f, 9f)
            )
        }
    }

    @Nested
    inner class OnPlayerSelectObjectTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun givenMapObjectItShouldCallOnPlayerSelectMapObjectHandler() {
            val mapObjectId = 1337
            val mapObject = mockk<MapObject>()
            every { entityResolver.run { mapObjectId.toMapObject() } } returns mapObject
            every { onPlayerSelectMapObjectHandler.onPlayerSelectMapObject(any(), any(), any(), any()) } just Runs

            callbackProcessor.onPlayerSelectObject(
                    playerid = playerId,
                    type = SAMPConstants.SELECT_OBJECT_GLOBAL_OBJECT,
                    objectid = mapObjectId,
                    modelid = 12345,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f
            )

            verify {
                onPlayerSelectMapObjectHandler.onPlayerSelectMapObject(player, mapObject, 12345, vector3DOf(1f, 2f, 3f))
            }
        }

        @Test
        fun givenPlayerMapObjectItShouldCallOnPlayerSelectPlayerMapObjectHandler() {
            val playerMapObjectId = 1337
            val playerMapObject = mockk<PlayerMapObject>()
            every { entityResolver.run { playerMapObjectId.toPlayerMapObject(player) } } returns playerMapObject
            every { onPlayerSelectPlayerMapObjectHandler.onPlayerSelectPlayerMapObject(any(), any(), any()) } just Runs

            callbackProcessor.onPlayerSelectObject(
                    playerid = playerId,
                    type = SAMPConstants.SELECT_OBJECT_PLAYER_OBJECT,
                    objectid = playerMapObjectId,
                    modelid = 12345,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f
            )

            verify {
                onPlayerSelectPlayerMapObjectHandler.onPlayerSelectPlayerMapObject(
                        playerMapObject,
                        12345,
                        vector3DOf(1f, 2f, 3f)
                )
            }
        }

    }

    @Nested
    inner class OnPlayerWeaponShotTests {

        private val playerId = 69
        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { entityResolver.run { playerId.toPlayer() } } returns player
        }

        @Test
        fun givenBulletHitTypeNoneItShouldCallHandlerWithNoHitTarget() {
            every {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(any(), any(), any(), any())
            } returns OnPlayerWeaponShotListener.Result.AllowDamage

            callbackProcessor.onPlayerWeaponShot(
                    playerid = playerId,
                    weaponid = WeaponModel.AK47.value,
                    hittype = BulletHitType.NONE.value,
                    hitid = 0,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f
            )

            verify {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(
                        player,
                        WeaponModel.AK47,
                        NoHitTarget,
                        vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun givenBulletHitTypePlayerItShouldCallHandlerWithPlayerHitTarget() {
            val hitPlayerId = 1234
            val hitPlayer = mockk<Player>()
            every { entityResolver.run { hitPlayerId.toPlayer() } } returns hitPlayer
            every {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(any(), any(), any(), any())
            } returns OnPlayerWeaponShotListener.Result.AllowDamage

            callbackProcessor.onPlayerWeaponShot(
                    playerid = playerId,
                    weaponid = WeaponModel.AK47.value,
                    hittype = BulletHitType.PLAYER.value,
                    hitid = hitPlayerId,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f
            )

            verify {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(
                        player,
                        WeaponModel.AK47,
                        PlayerHitTarget(hitPlayer),
                        vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun givenBulletHitTypeVehicleItShouldCallHandlerWithVehicleHitTarget() {
            val vehicleId = 1234
            val vehicle = mockk<Vehicle>()
            every { entityResolver.run { vehicleId.toVehicle() } } returns vehicle
            every {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(any(), any(), any(), any())
            } returns OnPlayerWeaponShotListener.Result.AllowDamage

            callbackProcessor.onPlayerWeaponShot(
                    playerid = playerId,
                    weaponid = WeaponModel.AK47.value,
                    hittype = BulletHitType.VEHICLE.value,
                    hitid = vehicleId,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f
            )

            verify {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(
                        player,
                        WeaponModel.AK47,
                        VehicleHitTarget(vehicle),
                        vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun givenBulletHitTypeMapObjectItShouldCallHandlerWithMapObjectHitTarget() {
            val mapObjectId = 1234
            val mapObject = mockk<MapObject>()
            every { entityResolver.run { mapObjectId.toMapObject() } } returns mapObject
            every {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(any(), any(), any(), any())
            } returns OnPlayerWeaponShotListener.Result.AllowDamage

            callbackProcessor.onPlayerWeaponShot(
                    playerid = playerId,
                    weaponid = WeaponModel.AK47.value,
                    hittype = BulletHitType.OBJECT.value,
                    hitid = mapObjectId,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f
            )

            verify {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(
                        player,
                        WeaponModel.AK47,
                        MapObjectHitTarget(mapObject),
                        vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun givenBulletHitTypePlayerMapObjectItShouldCallHandlerWithPlayerMapObjectHitTarget() {
            val playerMapObjectId = 1234
            val playerMapObject = mockk<PlayerMapObject>()
            every { entityResolver.run { playerMapObjectId.toPlayerMapObject(player) } } returns playerMapObject
            every {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(any(), any(), any(), any())
            } returns OnPlayerWeaponShotListener.Result.AllowDamage

            callbackProcessor.onPlayerWeaponShot(
                    playerid = playerId,
                    weaponid = WeaponModel.AK47.value,
                    hittype = BulletHitType.PLAYER_OBJECT.value,
                    hitid = playerMapObjectId,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f
            )

            verify {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(
                        player,
                        WeaponModel.AK47,
                        PlayerMapObjectHitTarget(playerMapObject),
                        vector3DOf(1f, 2f, 3f)
                )
            }
        }

        @Test
        fun givenHandlerThrowsAnExceptionReturnsTrue() {
            every {
                onPlayerWeaponShotHandler.onPlayerShotWeapon(any(), any(), any(), any())
            } throws Exception()

            val result = callbackProcessor.onPlayerWeaponShot(
                    playerid = playerId,
                    weaponid = WeaponModel.AK47.value,
                    hittype = BulletHitType.NONE.value,
                    hitid = 0,
                    fX = 1f,
                    fY = 2f,
                    fZ = 3f
            )

            assertThat(result)
                    .isTrue()
        }
    }

    @Test
    fun shouldCallOnPlayerRequestDownload() {
        val playerId = 69
        val player = mockk<Player>()
        every { entityResolver.run { playerId.toPlayer() } } returns player
        every { onPlayerRequestDownloadHandler.onPlayerRequestDownload(any(), any(), any()) } just Runs

        callbackProcessor.onPlayerRequestDownload(playerId, DownloadRequestType.TEXTURE_FILE.value, 12345)

        verify {
            onPlayerRequestDownloadHandler.onPlayerRequestDownload(player, DownloadRequestType.TEXTURE_FILE, 12345)
        }
    }

}