package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamInListener
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamOutListener
import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.callback.OnEnterExitModShopListener
import ch.leadrian.samp.kamp.core.api.callback.OnGameModeExitListener
import ch.leadrian.samp.kamp.core.api.callback.OnGameModeInitListener
import ch.leadrian.samp.kamp.core.api.callback.OnIncomingConnectionListener
import ch.leadrian.samp.kamp.core.api.callback.OnObjectMovedListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickMapListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerConnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDeathListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditAttachedObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditMapObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterVehicleListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitVehicleListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitedMenuListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageActorListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerGiveDamageListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerInteriorChangeListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerKeyStateChangeListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerObjectMovedListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerPickUpPickupListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestClassListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectMapObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectedMenuRowListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStateChangeListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStreamInListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStreamOutListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTakeDamageListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerTextListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerWeaponShotListener
import ch.leadrian.samp.kamp.core.api.callback.OnProcessTickListener
import ch.leadrian.samp.kamp.core.api.callback.OnRconCommandListener
import ch.leadrian.samp.kamp.core.api.callback.OnRconLoginAttemptListener
import ch.leadrian.samp.kamp.core.api.callback.OnTrailerUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnUnoccupiedVehicleUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDamageStatusUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDeathListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleModListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehiclePaintjobListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSirenStateChangeListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamInListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamOutListener
import ch.leadrian.samp.kamp.core.api.command.UnknownCommandHandler
import ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.Bone
import ch.leadrian.samp.kamp.core.api.constants.BulletHitType
import ch.leadrian.samp.kamp.core.api.constants.ClickPlayerSource
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import ch.leadrian.samp.kamp.core.api.entity.PlayerKeys
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerClassId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextDrawId
import ch.leadrian.samp.kamp.core.api.entity.id.TextDrawId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.UncaughtExceptionNotifier
import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.Server
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.AbstractDialog
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerClassRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.TextDrawRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import com.google.inject.Module
import com.google.inject.Stage
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import java.nio.file.Paths
import java.util.*
import javax.annotation.PreDestroy
import javax.inject.Singleton

internal class CallbackProcessorTest {

    private lateinit var callbackProcessor: CallbackProcessor

    private lateinit var callbackListenerManager: CallbackListenerManager
    private lateinit var server: Server
    private lateinit var uncaughtExceptionNotifier: UncaughtExceptionNotifier

    private val configProperties = Properties()
    private val dataDirectory = Paths.get(".", "Kamp", "data")

    @BeforeEach
    fun setUp() {
        configProperties["kamp.gamemode.class.name"] = "ch.leadrian.samp.kamp.core.runtime.callback.CallbackProcessorTest\$TestGameMode"
        configProperties["test.value.foo"] = "Foobar"
        configProperties["test.value.bar"] = "1337"
        val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>(relaxed = true) {
            every { getMaxPlayers() } returns 1000
        }
        server = Server.start(nativeFunctionExecutor, configProperties, dataDirectory, Stage.DEVELOPMENT)
        callbackProcessor = server.injector.getInstance()
        callbackListenerManager = server.injector.getInstance()
        uncaughtExceptionNotifier = server.injector.getInstance()
    }

    @Nested
    inner class OnProcessTickTests {

        @Test
        fun shouldCallOnProcessTick() {
            val onProcessTickListener = mockk<OnProcessTickListener>(relaxed = true)
            callbackListenerManager.register(onProcessTickListener)

            callbackProcessor.onProcessTick()

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onProcessTickListener.onProcessTick() }
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onProcessTickListener = mockk<OnProcessTickListener> {
                every { onProcessTick() } throws exception
            }
            callbackListenerManager.register(onProcessTickListener)

            val caughtThrowable = catchThrowable { callbackProcessor.onProcessTick() }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onProcessTickListener.onProcessTick() }
        }

    }

    @Nested
    inner class OnGameModeInitTests {

        @Test
        fun shouldCallOnGameModeInit() {
            val onGameModeInitListener = mockk<OnGameModeInitListener>(relaxed = true)
            callbackListenerManager.register(onGameModeInitListener)

            callbackProcessor.onGameModeInit()

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onGameModeInitListener.onGameModeInit() }
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onGameModeInitListener = mockk<OnGameModeInitListener> {
                every { onGameModeInit() } throws exception
            }
            callbackListenerManager.register(onGameModeInitListener)

            val caughtThrowable = catchThrowable { callbackProcessor.onGameModeInit() }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onGameModeInitListener.onGameModeInit() }
        }

    }

    @Nested
    inner class OnGameModeExitTests {

        @Test
        fun shouldCallOnGameModeExit() {
            val onGameModeExitListener = mockk<OnGameModeExitListener>(relaxed = true)
            callbackListenerManager.register(onGameModeExitListener)

            callbackProcessor.onGameModeExit()

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onGameModeExitListener.onGameModeExit() }
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onGameModeExitListener = mockk<OnGameModeExitListener> {
                every { onGameModeExit() } throws exception
            }
            callbackListenerManager.register(onGameModeExitListener)

            val caughtThrowable = catchThrowable { callbackProcessor.onGameModeExit() }

            assertThat(caughtThrowable)
                    .isNull()
            verify { onGameModeExitListener.onGameModeExit() }
            verify { uncaughtExceptionNotifier.notify(exception) }
        }

        @Test
        fun shouldStopServer() {
            val lifecycleAwareService = server.injector.getInstance<LifecycleAwareService>()

            callbackProcessor.onGameModeExit()

            assertThat(lifecycleAwareService.isShutdown)
                    .isTrue()
        }

    }

    @Nested
    inner class OnPlayerConnectTests {

        @Test
        fun shouldCallOnPlayerConnect() {
            val onPlayerConnectListener = mockk<OnPlayerConnectListener>(relaxed = true)
            callbackListenerManager.register(onPlayerConnectListener)

            callbackProcessor.onPlayerConnect(69)

            val slot = slot<Player>()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerConnectListener.onPlayerConnect(capture(slot)) }
            assertThat(slot.captured)
                    .satisfies {
                        assertThat(it.id)
                                .isEqualTo(PlayerId.valueOf(69))
                    }
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerConnectListener = mockk<OnPlayerConnectListener> {
                every { onPlayerConnect(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerConnectListener)

            val caughtThrowable = catchThrowable { callbackProcessor.onPlayerConnect(69) }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerConnectListener.onPlayerConnect(any()) }
        }
    }

    @Nested
    inner class OnPlayerDisconnectTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @ParameterizedTest
        @EnumSource(DisconnectReason::class)
        fun shouldCallOnPlayerDisconnect(reason: DisconnectReason) {
            val onPlayerDisconnectListener = mockk<OnPlayerDisconnectListener>(relaxed = true)
            callbackListenerManager.register(onPlayerDisconnectListener)

            callbackProcessor.onPlayerDisconnect(playerId, reason.value)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerDisconnectListener.onPlayerDisconnect(player, reason) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerDisconnectListener = mockk<OnPlayerDisconnectListener>(relaxed = true)
            callbackListenerManager.register(onPlayerDisconnectListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerDisconnect(500, DisconnectReason.QUIT.value)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerDisconnectListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerDisconnectListener = mockk<OnPlayerDisconnectListener> {
                every { onPlayerDisconnect(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerDisconnectListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerDisconnect(playerId, DisconnectReason.QUIT.value)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerDisconnectListener.onPlayerDisconnect(player, DisconnectReason.QUIT) }
        }

    }

    @Nested
    inner class OnPlayerSpawnTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerSpawn() {
            val onPlayerSpawnListener = mockk<OnPlayerSpawnListener>(relaxed = true)
            callbackListenerManager.register(onPlayerSpawnListener)

            callbackProcessor.onPlayerSpawn(playerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerSpawnListener.onPlayerSpawn(player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerSpawnListener = mockk<OnPlayerSpawnListener>(relaxed = true)
            callbackListenerManager.register(onPlayerSpawnListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerSpawn(500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerSpawnListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerSpawnListener = mockk<OnPlayerSpawnListener> {
                every { onPlayerSpawn(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerSpawnListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerSpawn(playerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerSpawnListener.onPlayerSpawn(player) }
        }

    }

    @Nested
    inner class OnPlayerDeathTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerDeath() {
            val onPlayerDeathListener = mockk<OnPlayerDeathListener>(relaxed = true)
            val killerId = 100
            val killer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(killerId))
            callbackListenerManager.register(onPlayerDeathListener)

            callbackProcessor.onPlayerDeath(playerId, killerId, WeaponModel.FLAMETHROWER.value)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerDeathListener.onPlayerDeath(player, killer, WeaponModel.FLAMETHROWER) }
        }

        @Test
        fun givenNoKillerItShouldCallOnPlayerDeath() {
            val onPlayerDeathListener = mockk<OnPlayerDeathListener>(relaxed = true)
            callbackListenerManager.register(onPlayerDeathListener)

            callbackProcessor.onPlayerDeath(playerId, SAMPConstants.INVALID_PLAYER_ID, WeaponModel.FLAMETHROWER.value)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerDeathListener.onPlayerDeath(player, null, WeaponModel.FLAMETHROWER) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerDeathListener = mockk<OnPlayerDeathListener>(relaxed = true)
            callbackListenerManager.register(onPlayerDeathListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerDeath(500, 100, WeaponModel.FLAMETHROWER.value)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerDeathListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerDeathListener = mockk<OnPlayerDeathListener> {
                every { onPlayerDeath(any(), any(), any()) } throws exception
            }
            val killerId = 100
            val killer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(killerId))
            callbackListenerManager.register(onPlayerDeathListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerDeath(playerId, killerId, WeaponModel.FLAMETHROWER.value)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerDeathListener.onPlayerDeath(player, killer, WeaponModel.FLAMETHROWER) }
        }

    }

    @Nested
    inner class OnVehicleSpawnTests {

        private lateinit var vehicle: Vehicle
        private val vehicleId = 69

        @BeforeEach
        fun setUp() {
            vehicle = mockk {
                every { id } returns VehicleId.valueOf(vehicleId)
                every { onSpawn() } just Runs
            }
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
        }

        @Test
        fun shouldCallOnVehicleSpawn() {
            val onVehicleSpawnListener = mockk<OnVehicleSpawnListener>(relaxed = true)
            callbackListenerManager.register(onVehicleSpawnListener)

            callbackProcessor.onVehicleSpawn(vehicleId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleSpawnListener.onVehicleSpawn(vehicle) }
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val onVehicleSpawnListener = mockk<OnVehicleSpawnListener>(relaxed = true)
            callbackListenerManager.register(onVehicleSpawnListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleSpawn(500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onVehicleSpawnListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehicleSpawnListener = mockk<OnVehicleSpawnListener> {
                every { onVehicleSpawn(any()) } throws exception
            }
            callbackListenerManager.register(onVehicleSpawnListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleSpawn(vehicleId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehicleSpawnListener.onVehicleSpawn(vehicle) }
        }

    }

    @Nested
    inner class OnVehicleDeathTests {

        private lateinit var vehicle: Vehicle
        private val vehicleId = 69

        @BeforeEach
        fun setUp() {
            vehicle = mockk {
                every { id } returns VehicleId.valueOf(vehicleId)
                every { onDeath(any<Player>()) } just Runs
            }
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
        }

        @Test
        fun shouldCallOnVehicleDeath() {
            val onVehicleDeathListener = mockk<OnVehicleDeathListener>(relaxed = true)
            val killerId = 100
            val killer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(killerId))
            callbackListenerManager.register(onVehicleDeathListener)

            callbackProcessor.onVehicleDeath(vehicleId, killerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleDeathListener.onVehicleDeath(vehicle, killer) }
        }

        @Test
        fun givenNoKillerItShouldCallOnVehicleDeath() {
            val onVehicleDeathListener = mockk<OnVehicleDeathListener>(relaxed = true)
            callbackListenerManager.register(onVehicleDeathListener)

            callbackProcessor.onVehicleDeath(vehicleId, SAMPConstants.INVALID_VEHICLE_ID)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleDeathListener.onVehicleDeath(vehicle, null) }
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val onVehicleDeathListener = mockk<OnVehicleDeathListener>(relaxed = true)
            callbackListenerManager.register(onVehicleDeathListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleDeath(500, 100)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onVehicleDeathListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehicleDeathListener = mockk<OnVehicleDeathListener> {
                every { onVehicleDeath(any(), any()) } throws exception
            }
            val killerId = 100
            val killer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(killerId))
            callbackListenerManager.register(onVehicleDeathListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleDeath(vehicleId, killerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehicleDeathListener.onVehicleDeath(vehicle, killer) }
        }

    }

    @Nested
    inner class OnPlayerTextTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerTextAndReturnTrue() {
            val onPlayerTextListener = mockk<OnPlayerTextListener> {
                every { onPlayerText(player, "hi there!") } returns OnPlayerTextListener.Result.Allowed
            }
            callbackListenerManager.register(onPlayerTextListener)

            val result = callbackProcessor.onPlayerText(playerId, "hi there!")

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerTextListener.onPlayerText(player, "hi there!") }
        }

        @Test
        fun shouldCallOnPlayerTextAndReturnFalse() {
            val onPlayerTextListener = mockk<OnPlayerTextListener> {
                every { onPlayerText(player, "hi there!") } returns OnPlayerTextListener.Result.Blocked
            }
            callbackListenerManager.register(onPlayerTextListener)

            val result = callbackProcessor.onPlayerText(playerId, "hi there!")

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerTextListener.onPlayerText(player, "hi there!") }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerTextListener = mockk<OnPlayerTextListener>(relaxed = true)
            callbackListenerManager.register(onPlayerTextListener)

            val result = callbackProcessor.onPlayerText(500, "hi there!")

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerTextListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerTextListener = mockk<OnPlayerTextListener> {
                every { onPlayerText(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerTextListener)

            val result = callbackProcessor.onPlayerText(playerId, "hi there!")

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerTextListener.onPlayerText(player, "hi there!") }
        }

    }

    @Nested
    inner class OnPlayerCommandTextTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerCommandTextAndReturnTrue() {
            val onPlayerCommandTextListener = mockk<OnPlayerCommandTextListener> {
                every { onPlayerCommandText(player, "/hi there") } returns OnPlayerCommandTextListener.Result.Processed
            }
            callbackListenerManager.register(onPlayerCommandTextListener)

            val result = callbackProcessor.onPlayerCommandText(playerId, "/hi there")

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerCommandTextListener.onPlayerCommandText(player, "/hi there") }
        }

        @Test
        fun shouldCallOnPlayerCommandTextAndReturnFalse() {
            val onPlayerCommandTextListener = mockk<OnPlayerCommandTextListener> {
                every { onPlayerCommandText(player, "/hi there") } returns OnPlayerCommandTextListener.Result.UnknownCommand
            }
            callbackListenerManager.register(onPlayerCommandTextListener)

            val result = callbackProcessor.onPlayerCommandText(playerId, "/hi there")

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerCommandTextListener.onPlayerCommandText(player, "/hi there") }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerCommandTextListener = mockk<OnPlayerCommandTextListener>(relaxed = true)
            callbackListenerManager.register(onPlayerCommandTextListener)

            val result = callbackProcessor.onPlayerCommandText(500, "/hi there")

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerCommandTextListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerCommandTextListener = mockk<OnPlayerCommandTextListener> {
                every { onPlayerCommandText(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerCommandTextListener)

            val result = callbackProcessor.onPlayerCommandText(playerId, "/hi there")

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerCommandTextListener.onPlayerCommandText(player, "/hi there") }
        }

    }

    @Nested
    inner class OnPlayerRequestClassTests {

        private lateinit var player: Player
        private val playerId = 69
        private val playerClassId = 127
        private val playerClass: PlayerClass = mockk()

        @BeforeEach
        fun setUp() {
            every { playerClass.id } returns PlayerClassId.valueOf(playerClassId)
            server.injector.getInstance<PlayerClassRegistry>().register(playerClass)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerRequestClassAndReturnTrue() {
            val onPlayerRequestClassListener = mockk<OnPlayerRequestClassListener> {
                every { onPlayerRequestClass(player, playerClass) } returns OnPlayerRequestClassListener.Result.Allow
            }
            callbackListenerManager.register(onPlayerRequestClassListener)

            val result = callbackProcessor.onPlayerRequestClass(playerId, playerClassId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerRequestClassListener.onPlayerRequestClass(player, playerClass) }
        }

        @Test
        fun shouldCallOnPlayerRequestClassAndReturnFalse() {
            val onPlayerRequestClassListener = mockk<OnPlayerRequestClassListener> {
                every { onPlayerRequestClass(player, playerClass) } returns OnPlayerRequestClassListener.Result.PreventSpawn
            }
            callbackListenerManager.register(onPlayerRequestClassListener)

            val result = callbackProcessor.onPlayerRequestClass(playerId, playerClassId)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerRequestClassListener.onPlayerRequestClass(player, playerClass) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerRequestClassListener = mockk<OnPlayerRequestClassListener>(relaxed = true)
            callbackListenerManager.register(onPlayerRequestClassListener)

            val result = callbackProcessor.onPlayerRequestClass(500, playerClassId)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerRequestClassListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerRequestClassListener = mockk<OnPlayerRequestClassListener> {
                every { onPlayerRequestClass(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerRequestClassListener)

            val result = callbackProcessor.onPlayerRequestClass(playerId, playerClassId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerRequestClassListener.onPlayerRequestClass(player, playerClass) }
        }

    }

    @Nested
    inner class OnPlayerEnterVehicleTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.onEnter(any(), any()) } just Runs
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldCallOnPlayerEnterVehicle(isPassenger: Boolean) {
            val onPlayerEnterVehicleListener = mockk<OnPlayerEnterVehicleListener>(relaxed = true)
            callbackListenerManager.register(onPlayerEnterVehicleListener)

            val result = callbackProcessor.onPlayerEnterVehicle(playerId, vehicleId, isPassenger)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerEnterVehicleListener.onPlayerEnterVehicle(player, vehicle, isPassenger) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerEnterVehicleListener = mockk<OnPlayerEnterVehicleListener>(relaxed = true)
            callbackListenerManager.register(onPlayerEnterVehicleListener)

            val result = callbackProcessor.onPlayerEnterVehicle(500, vehicleId, true)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerEnterVehicleListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onPlayerEnterVehicleListener = mockk<OnPlayerEnterVehicleListener> {
                every { onPlayerEnterVehicle(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerEnterVehicleListener)

            val result = callbackProcessor.onPlayerEnterVehicle(playerId, 500, true)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerEnterVehicleListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerEnterVehicleListener = mockk<OnPlayerEnterVehicleListener> {
                every { onPlayerEnterVehicle(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerEnterVehicleListener)

            val result = callbackProcessor.onPlayerEnterVehicle(playerId, vehicleId, true)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerEnterVehicleListener.onPlayerEnterVehicle(player, vehicle, true) }
        }

    }

    @Nested
    inner class OnPlayerExitVehicleTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.onExit(any<Player>()) } just Runs
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerExitVehicle() {
            val onPlayerExitVehicleListener = mockk<OnPlayerExitVehicleListener>(relaxed = true)
            callbackListenerManager.register(onPlayerExitVehicleListener)

            val result = callbackProcessor.onPlayerExitVehicle(playerId, vehicleId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerExitVehicleListener.onPlayerExitVehicle(player, vehicle) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerExitVehicleListener = mockk<OnPlayerExitVehicleListener>(relaxed = true)
            callbackListenerManager.register(onPlayerExitVehicleListener)

            val result = callbackProcessor.onPlayerExitVehicle(500, vehicleId)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerExitVehicleListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onPlayerExitVehicleListener = mockk<OnPlayerExitVehicleListener> {
                every { onPlayerExitVehicle(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerExitVehicleListener)

            val result = callbackProcessor.onPlayerExitVehicle(playerId, 500)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerExitVehicleListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerExitVehicleListener = mockk<OnPlayerExitVehicleListener> {
                every { onPlayerExitVehicle(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerExitVehicleListener)

            val result = callbackProcessor.onPlayerExitVehicle(playerId, vehicleId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerExitVehicleListener.onPlayerExitVehicle(player, vehicle) }
        }

    }

    @Nested
    inner class OnPlayerStateChangeTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerStateChange() {
            val onPlayerStateChangeListener = mockk<OnPlayerStateChangeListener>(relaxed = true)
            callbackListenerManager.register(onPlayerStateChangeListener)

            callbackProcessor.onPlayerStateChange(playerid = playerId, newstate = PlayerState.ON_FOOT.value, oldstate = PlayerState.DRIVER.value)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerStateChangeListener.onPlayerStateChange(player = player, newState = PlayerState.ON_FOOT, oldState = PlayerState.DRIVER)
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerStateChangeListener = mockk<OnPlayerStateChangeListener>(relaxed = true)
            callbackListenerManager.register(onPlayerStateChangeListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerStateChange(playerid = 500, newstate = PlayerState.ON_FOOT.value, oldstate = PlayerState.DRIVER.value)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerStateChangeListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerStateChangeListener = mockk<OnPlayerStateChangeListener> {
                every { onPlayerStateChange(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerStateChangeListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerStateChange(playerid = playerId, newstate = PlayerState.ON_FOOT.value, oldstate = PlayerState.DRIVER.value)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerStateChangeListener.onPlayerStateChange(player = player, newState = PlayerState.ON_FOOT, oldState = PlayerState.DRIVER) }
        }

    }

    @Nested
    inner class OnPlayerEnterCheckpointTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerEnterCheckpoint() {
            val onPlayerEnterCheckpointListener = mockk<OnPlayerEnterCheckpointListener>(relaxed = true)
            callbackListenerManager.register(onPlayerEnterCheckpointListener)

            callbackProcessor.onPlayerEnterCheckpoint(playerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerEnterCheckpointListener.onPlayerEnterCheckpoint(player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerEnterCheckpointListener = mockk<OnPlayerEnterCheckpointListener>(relaxed = true)
            callbackListenerManager.register(onPlayerEnterCheckpointListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerEnterCheckpoint(500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerEnterCheckpointListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerEnterCheckpointListener = mockk<OnPlayerEnterCheckpointListener> {
                every { onPlayerEnterCheckpoint(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerEnterCheckpointListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerEnterCheckpoint(playerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerEnterCheckpointListener.onPlayerEnterCheckpoint(player) }
        }

    }

    @Nested
    inner class OnPlayerLeaveCheckpointTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerLeaveCheckpoint() {
            val onPlayerLeaveCheckpointListener = mockk<OnPlayerLeaveCheckpointListener>(relaxed = true)
            callbackListenerManager.register(onPlayerLeaveCheckpointListener)

            callbackProcessor.onPlayerLeaveCheckpoint(playerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerLeaveCheckpointListener.onPlayerLeaveCheckpoint(player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerLeaveCheckpointListener = mockk<OnPlayerLeaveCheckpointListener>(relaxed = true)
            callbackListenerManager.register(onPlayerLeaveCheckpointListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerLeaveCheckpoint(500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerLeaveCheckpointListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerLeaveCheckpointListener = mockk<OnPlayerLeaveCheckpointListener> {
                every { onPlayerLeaveCheckpoint(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerLeaveCheckpointListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerLeaveCheckpoint(playerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerLeaveCheckpointListener.onPlayerLeaveCheckpoint(player) }
        }

    }

    @Nested
    inner class OnPlayerEnterRaceCheckpointTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerEnterRaceCheckpoint() {
            val onPlayerEnterRaceCheckpointListener = mockk<OnPlayerEnterRaceCheckpointListener>(relaxed = true)
            callbackListenerManager.register(onPlayerEnterRaceCheckpointListener)

            callbackProcessor.onPlayerEnterRaceCheckpoint(playerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerEnterRaceCheckpointListener.onPlayerEnterRaceCheckpoint(player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerEnterRaceCheckpointListener = mockk<OnPlayerEnterRaceCheckpointListener>(relaxed = true)
            callbackListenerManager.register(onPlayerEnterRaceCheckpointListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerEnterRaceCheckpoint(500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerEnterRaceCheckpointListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerEnterRaceCheckpointListener = mockk<OnPlayerEnterRaceCheckpointListener> {
                every { onPlayerEnterRaceCheckpoint(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerEnterRaceCheckpointListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerEnterRaceCheckpoint(playerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerEnterRaceCheckpointListener.onPlayerEnterRaceCheckpoint(player) }
        }

    }

    @Nested
    inner class OnPlayerLeaveRaceCheckpointTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerLeaveRaceCheckpoint() {
            val onPlayerLeaveRaceCheckpointListener = mockk<OnPlayerLeaveRaceCheckpointListener>(relaxed = true)
            callbackListenerManager.register(onPlayerLeaveRaceCheckpointListener)

            callbackProcessor.onPlayerLeaveRaceCheckpoint(playerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerLeaveRaceCheckpointListener.onPlayerLeaveRaceCheckpoint(player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerLeaveRaceCheckpointListener = mockk<OnPlayerLeaveRaceCheckpointListener>(relaxed = true)
            callbackListenerManager.register(onPlayerLeaveRaceCheckpointListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerLeaveRaceCheckpoint(500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerLeaveRaceCheckpointListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerLeaveRaceCheckpointListener = mockk<OnPlayerLeaveRaceCheckpointListener> {
                every { onPlayerLeaveRaceCheckpoint(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerLeaveRaceCheckpointListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerLeaveRaceCheckpoint(playerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerLeaveRaceCheckpointListener.onPlayerLeaveRaceCheckpoint(player) }
        }

    }

    @Nested
    inner class OnRconCommandTests {

        @Test
        fun shouldCallOnRconCommandAndReturnTrue() {
            val onRconCommandListener = mockk<OnRconCommandListener> {
                every { onRconCommand("/hi there") } returns OnRconCommandListener.Result.Processed
            }
            callbackListenerManager.register(onRconCommandListener)

            val result = callbackProcessor.onRconCommand("/hi there")

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onRconCommandListener.onRconCommand("/hi there") }
        }

        @Test
        fun shouldCallOnRconCommandAndReturnFalse() {
            val onRconCommandListener = mockk<OnRconCommandListener> {
                every { onRconCommand("/hi there") } returns OnRconCommandListener.Result.UnknownCommand
            }
            callbackListenerManager.register(onRconCommandListener)

            val result = callbackProcessor.onRconCommand("/hi there")

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onRconCommandListener.onRconCommand("/hi there") }
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onRconCommandListener = mockk<OnRconCommandListener> {
                every { onRconCommand(any()) } throws exception
            }
            callbackListenerManager.register(onRconCommandListener)

            val result = callbackProcessor.onRconCommand("/hi there")

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onRconCommandListener.onRconCommand("/hi there") }
        }

    }

    @Nested
    inner class OnPlayerRequestSpawnTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerRequestSpawnAndReturnTrue() {
            val onPlayerRequestSpawnListener = mockk<OnPlayerRequestSpawnListener> {
                every { onPlayerRequestSpawn(player) } returns OnPlayerRequestSpawnListener.Result.Granted
            }
            callbackListenerManager.register(onPlayerRequestSpawnListener)

            val result = callbackProcessor.onPlayerRequestSpawn(playerId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerRequestSpawnListener.onPlayerRequestSpawn(player) }
        }

        @Test
        fun shouldCallOnPlayerRequestSpawnAndReturnFalse() {
            val onPlayerRequestSpawnListener = mockk<OnPlayerRequestSpawnListener> {
                every { onPlayerRequestSpawn(player) } returns OnPlayerRequestSpawnListener.Result.Denied
            }
            callbackListenerManager.register(onPlayerRequestSpawnListener)

            val result = callbackProcessor.onPlayerRequestSpawn(playerId)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerRequestSpawnListener.onPlayerRequestSpawn(player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerRequestSpawnListener = mockk<OnPlayerRequestSpawnListener>(relaxed = true)
            callbackListenerManager.register(onPlayerRequestSpawnListener)

            val result = callbackProcessor.onPlayerRequestSpawn(500)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerRequestSpawnListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerRequestSpawnListener = mockk<OnPlayerRequestSpawnListener> {
                every { onPlayerRequestSpawn(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerRequestSpawnListener)

            val result = callbackProcessor.onPlayerRequestSpawn(playerId)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerRequestSpawnListener.onPlayerRequestSpawn(player) }
        }

    }

    @Nested
    inner class OnMapObjectMovedTests {

        private lateinit var mapObject: MapObject
        private val mapObjectId = 69

        @BeforeEach
        fun setUp() {
            mapObject = mockk {
                every { id } returns MapObjectId.valueOf(mapObjectId)
                every { onMoved() } just Runs
            }
            server.injector.getInstance<MapObjectRegistry>().register(mapObject)
        }

        @Test
        fun shouldCallOnMapObjectMoved() {
            val onMapObjectMovedListener = mockk<OnObjectMovedListener>(relaxed = true)
            callbackListenerManager.register(onMapObjectMovedListener)

            callbackProcessor.onObjectMoved(mapObjectId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onMapObjectMovedListener.onObjectMoved(mapObject) }
        }

        @Test
        fun givenInvalidMapObjectIdItShouldThrowAndCatchException() {
            val onMapObjectMovedListener = mockk<OnObjectMovedListener>(relaxed = true)
            callbackListenerManager.register(onMapObjectMovedListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onObjectMoved(500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onMapObjectMovedListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid map object ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onMapObjectMovedListener = mockk<OnObjectMovedListener> {
                every { onObjectMoved(any()) } throws exception
            }
            callbackListenerManager.register(onMapObjectMovedListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onObjectMoved(mapObjectId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onMapObjectMovedListener.onObjectMoved(mapObject) }
        }

    }

    @Nested
    inner class OnPlayerMapPlayerObjectMovedTests {

        private lateinit var player: Player
        private val playerId = 50
        private lateinit var playerMapObject: PlayerMapObject
        private val playerMapObjectId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
            playerMapObject = mockk {
                every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
                every { onMoved() } just Runs
            }
            player.playerMapObjectRegistry.register(playerMapObject)
        }

        @Test
        fun shouldCallOnPlayerMapPlayerObjectMoved() {
            val onPlayerMapPlayerObjectMovedListener = mockk<OnPlayerObjectMovedListener>(relaxed = true)
            callbackListenerManager.register(onPlayerMapPlayerObjectMovedListener)

            callbackProcessor.onPlayerObjectMoved(playerId, playerMapObjectId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerMapPlayerObjectMovedListener.onPlayerObjectMoved(playerMapObject) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerMapPlayerObjectMovedListener = mockk<OnPlayerObjectMovedListener>(relaxed = true)
            callbackListenerManager.register(onPlayerMapPlayerObjectMovedListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerObjectMoved(500, playerMapObjectId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerMapPlayerObjectMovedListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidPlayerMapObjectIdItShouldThrowAndCatchException() {
            val onPlayerMapPlayerObjectMovedListener = mockk<OnPlayerObjectMovedListener>(relaxed = true)
            callbackListenerManager.register(onPlayerMapPlayerObjectMovedListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerObjectMoved(playerId, 500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerMapPlayerObjectMovedListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player map object ID 500 for player ID 50")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerMapPlayerObjectMovedListener = mockk<OnPlayerObjectMovedListener> {
                every { onPlayerObjectMoved(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerMapPlayerObjectMovedListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerObjectMoved(playerId, playerMapObjectId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerMapPlayerObjectMovedListener.onPlayerObjectMoved(playerMapObject) }
        }

    }

    @Nested
    inner class OnPlayerPickUpPickupTests {

        private lateinit var player: Player
        private val playerId = 69
        private val pickupId = 127
        private val pickup: Pickup = mockk()

        @BeforeEach
        fun setUp() {
            every { pickup.onPickUp(any<Player>()) } just Runs
            every { pickup.id } returns PickupId.valueOf(pickupId)
            server.injector.getInstance<PickupRegistry>().register(pickup)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerPickUpPickup() {
            val onPlayerPickUpPickupListener = mockk<OnPlayerPickUpPickupListener>(relaxed = true)
            callbackListenerManager.register(onPlayerPickUpPickupListener)

            val result = callbackProcessor.onPlayerPickUpPickup(playerId, pickupId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerPickUpPickupListener.onPlayerPickUpPickup(player, pickup) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerPickUpPickupListener = mockk<OnPlayerPickUpPickupListener>(relaxed = true)
            callbackListenerManager.register(onPlayerPickUpPickupListener)

            val result = callbackProcessor.onPlayerPickUpPickup(500, pickupId)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerPickUpPickupListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidPickupIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onPlayerPickUpPickupListener = mockk<OnPlayerPickUpPickupListener> {
                every { onPlayerPickUpPickup(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerPickUpPickupListener)

            val result = callbackProcessor.onPlayerPickUpPickup(playerId, 500)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerPickUpPickupListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid pickup ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerPickUpPickupListener = mockk<OnPlayerPickUpPickupListener> {
                every { onPlayerPickUpPickup(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerPickUpPickupListener)

            val result = callbackProcessor.onPlayerPickUpPickup(playerId, pickupId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerPickUpPickupListener.onPlayerPickUpPickup(player, pickup) }
        }

    }

    @Nested
    inner class OnVehicleModTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnVehicleModAndReturnTrue() {
            val onVehicleModListener = mockk<OnVehicleModListener> {
                every { onVehicleMod(any(), any(), any()) } returns OnVehicleModListener.Result.Sync
            }
            callbackListenerManager.register(onVehicleModListener)

            val result = callbackProcessor.onVehicleMod(playerId, vehicleId, VehicleComponentModel.EXHAUST_ALIEN_ELEGY.value)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleModListener.onVehicleMod(player, vehicle, VehicleComponentModel.EXHAUST_ALIEN_ELEGY) }
        }

        @Test
        fun shouldCallOnVehicleModAndReturnFalse() {
            val onVehicleModListener = mockk<OnVehicleModListener> {
                every { onVehicleMod(any(), any(), any()) } returns OnVehicleModListener.Result.Desync
            }
            callbackListenerManager.register(onVehicleModListener)

            val result = callbackProcessor.onVehicleMod(playerId, vehicleId, VehicleComponentModel.EXHAUST_ALIEN_ELEGY.value)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleModListener.onVehicleMod(player, vehicle, VehicleComponentModel.EXHAUST_ALIEN_ELEGY) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onVehicleModListener = mockk<OnVehicleModListener>(relaxed = true)
            callbackListenerManager.register(onVehicleModListener)

            val result = callbackProcessor.onVehicleMod(500, vehicleId, VehicleComponentModel.EXHAUST_ALIEN_ELEGY.value)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onVehicleModListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onVehicleModListener = mockk<OnVehicleModListener> {
                every { onVehicleMod(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleModListener)

            val result = callbackProcessor.onVehicleMod(playerId, 500, VehicleComponentModel.EXHAUST_ALIEN_ELEGY.value)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onVehicleModListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehicleModListener = mockk<OnVehicleModListener> {
                every { onVehicleMod(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleModListener)

            val result = callbackProcessor.onVehicleMod(playerId, vehicleId, VehicleComponentModel.EXHAUST_ALIEN_ELEGY.value)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehicleModListener.onVehicleMod(player, vehicle, VehicleComponentModel.EXHAUST_ALIEN_ELEGY) }
        }

    }

    @Nested
    inner class OnEnterExitModShopTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldCallOnEnterExitModShop(entered: Boolean) {
            val onEnterExitModShopListener = mockk<OnEnterExitModShopListener>(relaxed = true)
            callbackListenerManager.register(onEnterExitModShopListener)

            callbackProcessor.onEnterExitModShop(playerId, entered, 13)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onEnterExitModShopListener.onEnterExitModShop(player, entered, 13) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onEnterExitModShopListener = mockk<OnEnterExitModShopListener>(relaxed = true)
            callbackListenerManager.register(onEnterExitModShopListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onEnterExitModShop(500, true, 13)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onEnterExitModShopListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onEnterExitModShopListener = mockk<OnEnterExitModShopListener> {
                every { onEnterExitModShop(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onEnterExitModShopListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onEnterExitModShop(playerId, true, 13)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onEnterExitModShopListener.onEnterExitModShop(player, true, 13) }
        }

    }

    @Nested
    inner class OnVehiclePaintjobTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.onEnter(any(), any()) } just Runs
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnVehiclePaintjob() {
            val onVehiclePaintjobListener = mockk<OnVehiclePaintjobListener>(relaxed = true)
            callbackListenerManager.register(onVehiclePaintjobListener)

            val result = callbackProcessor.onVehiclePaintjob(playerId, vehicleId, 3)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehiclePaintjobListener.onVehiclePaintjob(player, vehicle, 3) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onVehiclePaintjobListener = mockk<OnVehiclePaintjobListener>(relaxed = true)
            callbackListenerManager.register(onVehiclePaintjobListener)

            val result = callbackProcessor.onVehiclePaintjob(500, vehicleId, 3)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onVehiclePaintjobListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onVehiclePaintjobListener = mockk<OnVehiclePaintjobListener> {
                every { onVehiclePaintjob(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehiclePaintjobListener)

            val result = callbackProcessor.onVehiclePaintjob(playerId, 500, 3)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onVehiclePaintjobListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehiclePaintjobListener = mockk<OnVehiclePaintjobListener> {
                every { onVehiclePaintjob(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehiclePaintjobListener)

            val result = callbackProcessor.onVehiclePaintjob(playerId, vehicleId, 3)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehiclePaintjobListener.onVehiclePaintjob(player, vehicle, 3) }
        }

    }

    @Nested
    inner class OnVehicleResprayTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnVehicleResprayAndReturnTrue() {
            val onVehicleResprayListener = mockk<OnVehicleResprayListener> {
                every { onVehicleRespray(any(), any(), any()) } returns OnVehicleResprayListener.Result.Sync
            }
            callbackListenerManager.register(onVehicleResprayListener)

            val result = callbackProcessor.onVehicleRespray(playerId, vehicleId, 3, 6)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleResprayListener.onVehicleRespray(player, vehicle, vehicleColorsOf(3, 6)) }
        }

        @Test
        fun shouldCallOnVehicleResprayAndReturnFalse() {
            val onVehicleResprayListener = mockk<OnVehicleResprayListener> {
                every { onVehicleRespray(any(), any(), any()) } returns OnVehicleResprayListener.Result.Desync
            }
            callbackListenerManager.register(onVehicleResprayListener)

            val result = callbackProcessor.onVehicleRespray(playerId, vehicleId, 3, 6)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleResprayListener.onVehicleRespray(player, vehicle, vehicleColorsOf(3, 6)) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onVehicleResprayListener = mockk<OnVehicleResprayListener>(relaxed = true)
            callbackListenerManager.register(onVehicleResprayListener)

            val result = callbackProcessor.onVehicleRespray(500, vehicleId, 3, 6)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onVehicleResprayListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onVehicleResprayListener = mockk<OnVehicleResprayListener> {
                every { onVehicleRespray(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleResprayListener)

            val result = callbackProcessor.onVehicleRespray(playerId, 500, 3, 6)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onVehicleResprayListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehicleResprayListener = mockk<OnVehicleResprayListener> {
                every { onVehicleRespray(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleResprayListener)

            val result = callbackProcessor.onVehicleRespray(playerId, vehicleId, 3, 6)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehicleResprayListener.onVehicleRespray(player, vehicle, vehicleColorsOf(3, 6)) }
        }

    }

    @Nested
    inner class OnVehicleDamageStatusUpdateTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.onExit(any<Player>()) } just Runs
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnVehicleDamageStatusUpdate() {
            val onVehicleDamageStatusUpdateListener = mockk<OnVehicleDamageStatusUpdateListener>(relaxed = true)
            callbackListenerManager.register(onVehicleDamageStatusUpdateListener)

            val result = callbackProcessor.onVehicleDamageStatusUpdate(vehicleId, playerId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleDamageStatusUpdateListener.onVehicleDamageStatusUpdate(vehicle, player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onVehicleDamageStatusUpdateListener = mockk<OnVehicleDamageStatusUpdateListener>(relaxed = true)
            callbackListenerManager.register(onVehicleDamageStatusUpdateListener)

            val result = callbackProcessor.onVehicleDamageStatusUpdate(vehicleId, 500)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onVehicleDamageStatusUpdateListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onVehicleDamageStatusUpdateListener = mockk<OnVehicleDamageStatusUpdateListener> {
                every { onVehicleDamageStatusUpdate(any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleDamageStatusUpdateListener)

            val result = callbackProcessor.onVehicleDamageStatusUpdate(500, playerId)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onVehicleDamageStatusUpdateListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehicleDamageStatusUpdateListener = mockk<OnVehicleDamageStatusUpdateListener> {
                every { onVehicleDamageStatusUpdate(any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleDamageStatusUpdateListener)

            val result = callbackProcessor.onVehicleDamageStatusUpdate(vehicleId, playerId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehicleDamageStatusUpdateListener.onVehicleDamageStatusUpdate(vehicle, player) }
        }

    }

    @Nested
    inner class OnUnoccupiedVehicleUpdateTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.onExit(any<Player>()) } just Runs
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnUnoccupiedVehicleUpdate() {
            val onUnoccupiedVehicleUpdateListener = mockk<OnUnoccupiedVehicleUpdateListener>(relaxed = true)
            callbackListenerManager.register(onUnoccupiedVehicleUpdateListener)

            val result = callbackProcessor.onUnoccupiedVehicleUpdate(
                    vehicleid = vehicleId,
                    playerid = playerId,
                    passenger_seat = 1,
                    new_x = 1f,
                    new_y = 2f,
                    new_z = 3f,
                    vel_x = 4f,
                    vel_y = 5f,
                    vel_z = 6f
            )

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onUnoccupiedVehicleUpdateListener.onUnoccupiedVehicleUpdate(
                        vehicle = vehicle,
                        player = player,
                        passengerSeat = 1,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        velocity = vector3DOf(4f, 5f, 6f)
                )
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onUnoccupiedVehicleUpdateListener = mockk<OnUnoccupiedVehicleUpdateListener>(relaxed = true)
            callbackListenerManager.register(onUnoccupiedVehicleUpdateListener)

            val result = callbackProcessor.onUnoccupiedVehicleUpdate(
                    vehicleid = vehicleId,
                    playerid = 500,
                    passenger_seat = 1,
                    new_x = 1f,
                    new_y = 2f,
                    new_z = 3f,
                    vel_x = 4f,
                    vel_y = 5f,
                    vel_z = 6f
            )

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onUnoccupiedVehicleUpdateListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onUnoccupiedVehicleUpdateListener = mockk<OnUnoccupiedVehicleUpdateListener> {
                every { onUnoccupiedVehicleUpdate(any(), any(), any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onUnoccupiedVehicleUpdateListener)

            val result = callbackProcessor.onUnoccupiedVehicleUpdate(
                    vehicleid = 500,
                    playerid = playerId,
                    passenger_seat = 1,
                    new_x = 1f,
                    new_y = 2f,
                    new_z = 3f,
                    vel_x = 4f,
                    vel_y = 5f,
                    vel_z = 6f
            )

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onUnoccupiedVehicleUpdateListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onUnoccupiedVehicleUpdateListener = mockk<OnUnoccupiedVehicleUpdateListener> {
                every { onUnoccupiedVehicleUpdate(any(), any(), any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onUnoccupiedVehicleUpdateListener)

            val result = callbackProcessor.onUnoccupiedVehicleUpdate(
                    vehicleid = vehicleId,
                    playerid = playerId,
                    passenger_seat = 1,
                    new_x = 1f,
                    new_y = 2f,
                    new_z = 3f,
                    vel_x = 4f,
                    vel_y = 5f,
                    vel_z = 6f
            )

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify {
                onUnoccupiedVehicleUpdateListener.onUnoccupiedVehicleUpdate(
                        vehicle = vehicle,
                        player = player,
                        passengerSeat = 1,
                        coordinates = vector3DOf(1f, 2f, 3f),
                        velocity = vector3DOf(4f, 5f, 6f)
                )
            }
        }

    }

    @Nested
    inner class OnPlayerSelectedMenuRowTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerSelectedMenuRow() {
            val onPlayerSelectedMenuRowListener = mockk<OnPlayerSelectedMenuRowListener>(relaxed = true)
            callbackListenerManager.register(onPlayerSelectedMenuRowListener)

            callbackProcessor.onPlayerSelectedMenuRow(playerId, 5)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerSelectedMenuRowListener.onPlayerSelectedMenuRow(player, 5) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerSelectedMenuRowListener = mockk<OnPlayerSelectedMenuRowListener>(relaxed = true)
            callbackListenerManager.register(onPlayerSelectedMenuRowListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerSelectedMenuRow(500, 5)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerSelectedMenuRowListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerSelectedMenuRowListener = mockk<OnPlayerSelectedMenuRowListener> {
                every { onPlayerSelectedMenuRow(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerSelectedMenuRowListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerSelectedMenuRow(playerId, 5)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerSelectedMenuRowListener.onPlayerSelectedMenuRow(player, 5) }
        }

    }

    @Nested
    inner class OnPlayerExitedMenuTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerExitedMenu() {
            val onPlayerExitedMenuListener = mockk<OnPlayerExitedMenuListener>(relaxed = true)
            callbackListenerManager.register(onPlayerExitedMenuListener)

            callbackProcessor.onPlayerExitedMenu(playerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerExitedMenuListener.onPlayerExitedMenu(player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerExitedMenuListener = mockk<OnPlayerExitedMenuListener>(relaxed = true)
            callbackListenerManager.register(onPlayerExitedMenuListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerExitedMenu(500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerExitedMenuListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerExitedMenuListener = mockk<OnPlayerExitedMenuListener> {
                every { onPlayerExitedMenu(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerExitedMenuListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerExitedMenu(playerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerExitedMenuListener.onPlayerExitedMenu(player) }
        }

    }

    @Nested
    inner class OnPlayerInteriorChangeTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerInteriorChange() {
            val onPlayerInteriorChangeListener = mockk<OnPlayerInteriorChangeListener>(relaxed = true)
            callbackListenerManager.register(onPlayerInteriorChangeListener)

            callbackProcessor.onPlayerInteriorChange(playerid = playerId, newinteriorid = 0, oldinteriorid = 15)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerInteriorChangeListener.onPlayerInteriorChange(player = player, newInteriorId = 0, oldInteriorId = 15) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerInteriorChangeListener = mockk<OnPlayerInteriorChangeListener>(relaxed = true)
            callbackListenerManager.register(onPlayerInteriorChangeListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerInteriorChange(playerid = 500, newinteriorid = 0, oldinteriorid = 15)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerInteriorChangeListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerInteriorChangeListener = mockk<OnPlayerInteriorChangeListener> {
                every { onPlayerInteriorChange(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerInteriorChangeListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerInteriorChange(playerid = playerId, newinteriorid = 0, oldinteriorid = 15)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerInteriorChangeListener.onPlayerInteriorChange(player = player, newInteriorId = 0, oldInteriorId = 15) }
        }

    }

    @Nested
    inner class OnPlayerKeyStateChangeTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerKeyStateChange() {
            val onPlayerKeyStateChangeListener = mockk<OnPlayerKeyStateChangeListener>(relaxed = true)
            callbackListenerManager.register(onPlayerKeyStateChangeListener)

            callbackProcessor.onPlayerKeyStateChange(playerid = playerId, newkeys = 64, oldkeys = 256)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerKeyStateChangeListener.onPlayerKeyStateChange(
                        oldKeys = PlayerKeys(256, 0, 0, player),
                        newKeys = PlayerKeys(64, 0, 0, player)
                )
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerKeyStateChangeListener = mockk<OnPlayerKeyStateChangeListener>(relaxed = true)
            callbackListenerManager.register(onPlayerKeyStateChangeListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerKeyStateChange(playerid = 500, newkeys = 64, oldkeys = 256)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerKeyStateChangeListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerKeyStateChangeListener = mockk<OnPlayerKeyStateChangeListener> {
                every { onPlayerKeyStateChange(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerKeyStateChangeListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerKeyStateChange(playerid = playerId, newkeys = 64, oldkeys = 256)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify {
                onPlayerKeyStateChangeListener.onPlayerKeyStateChange(
                        oldKeys = PlayerKeys(256, 0, 0, player),
                        newKeys = PlayerKeys(64, 0, 0, player)
                )
            }
        }

    }

    @Nested
    inner class OnRconLoginAttemptTests {

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldCallOnRconLoginAttemptAndReturnTrue(success: Boolean) {
            val onRconLoginAttemptListener = mockk<OnRconLoginAttemptListener>(relaxed = true)
            callbackListenerManager.register(onRconLoginAttemptListener)

            val result = callbackProcessor.onRconLoginAttempt("127.0.0.1", "hahaha", success)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onRconLoginAttemptListener.onRconLoginAttempt("127.0.0.1", "hahaha", success) }
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onRconLoginAttemptListener = mockk<OnRconLoginAttemptListener> {
                every { onRconLoginAttempt(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onRconLoginAttemptListener)

            val result = callbackProcessor.onRconLoginAttempt("127.0.0.1", "hahaha", true)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onRconLoginAttemptListener.onRconLoginAttempt("127.0.0.1", "hahaha", true) }
        }

    }

    @Nested
    inner class OnPlayerUpdateTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerUpdateAndReturnTrue() {
            val onPlayerUpdateListener = mockk<OnPlayerUpdateListener> {
                every { onPlayerUpdate(any()) } returns OnPlayerUpdateListener.Result.Sync
            }
            callbackListenerManager.register(onPlayerUpdateListener)

            val result = callbackProcessor.onPlayerUpdate(playerId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerUpdateListener.onPlayerUpdate(player) }
        }

        @Test
        fun shouldCallOnPlayerUpdateAndReturnFalse() {
            val onPlayerUpdateListener = mockk<OnPlayerUpdateListener> {
                every { onPlayerUpdate(any()) } returns OnPlayerUpdateListener.Result.Desync
            }
            callbackListenerManager.register(onPlayerUpdateListener)

            val result = callbackProcessor.onPlayerUpdate(playerId)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerUpdateListener.onPlayerUpdate(player) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerUpdateListener = mockk<OnPlayerUpdateListener>(relaxed = true)
            callbackListenerManager.register(onPlayerUpdateListener)

            val result = callbackProcessor.onPlayerUpdate(500)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onPlayerUpdateListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerUpdateListener = mockk<OnPlayerUpdateListener> {
                every { onPlayerUpdate(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerUpdateListener)

            val result = callbackProcessor.onPlayerUpdate(playerId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerUpdateListener.onPlayerUpdate(player) }
        }

    }

    @Nested
    inner class OnPlayerStreamInTests {

        private lateinit var player: Player
        private val playerId = 69
        private lateinit var forPlayer: Player
        private val forPlayerId = 150

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
            forPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(forPlayerId))
        }

        @Test
        fun shouldCallOnPlayerStreamIn() {
            val onPlayerStreamInListener = mockk<OnPlayerStreamInListener>(relaxed = true)
            callbackListenerManager.register(onPlayerStreamInListener)

            callbackProcessor.onPlayerStreamIn(playerid = playerId, forplayerid = forPlayerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerStreamInListener.onPlayerStreamIn(player = player, forPlayer = forPlayer) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerStreamInListener = mockk<OnPlayerStreamInListener>(relaxed = true)
            callbackListenerManager.register(onPlayerStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerStreamIn(playerid = 500, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerStreamInListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidForPlayerIdItShouldThrowAndCatchException() {
            val onPlayerStreamInListener = mockk<OnPlayerStreamInListener>(relaxed = true)
            callbackListenerManager.register(onPlayerStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerStreamIn(playerid = playerId, forplayerid = 500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerStreamInListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerStreamInListener = mockk<OnPlayerStreamInListener> {
                every { onPlayerStreamIn(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerStreamIn(playerid = playerId, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerStreamInListener.onPlayerStreamIn(player = player, forPlayer = forPlayer) }
        }

    }

    @Nested
    inner class OnPlayerStreamOutTests {

        private lateinit var player: Player
        private val playerId = 69
        private lateinit var forPlayer: Player
        private val forPlayerId = 150

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
            forPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(forPlayerId))
        }

        @Test
        fun shouldCallOnPlayerStreamOut() {
            val onPlayerStreamOutListener = mockk<OnPlayerStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onPlayerStreamOutListener)

            callbackProcessor.onPlayerStreamOut(playerid = playerId, forplayerid = forPlayerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerStreamOutListener.onPlayerStreamOut(player = player, forPlayer = forPlayer) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerStreamOutListener = mockk<OnPlayerStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onPlayerStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerStreamOut(playerid = 500, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerStreamOutListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidForPlayerIdItShouldThrowAndCatchException() {
            val onPlayerStreamOutListener = mockk<OnPlayerStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onPlayerStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerStreamOut(playerid = playerId, forplayerid = 500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerStreamOutListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerStreamOutListener = mockk<OnPlayerStreamOutListener> {
                every { onPlayerStreamOut(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerStreamOut(playerid = playerId, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerStreamOutListener.onPlayerStreamOut(player = player, forPlayer = forPlayer) }
        }

    }

    @Nested
    inner class OnVehicleStreamInTests {

        private val vehicle = mockk<Vehicle>()
        private val vehicleId = 69
        private lateinit var forPlayer: Player
        private val forPlayerId = 150

        @BeforeEach
        fun setUp() {
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            forPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(forPlayerId))
        }

        @Test
        fun shouldCallOnVehicleStreamIn() {
            val onVehicleStreamInListener = mockk<OnVehicleStreamInListener>(relaxed = true)
            callbackListenerManager.register(onVehicleStreamInListener)

            callbackProcessor.onVehicleStreamIn(vehicleid = vehicleId, forplayerid = forPlayerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleStreamInListener.onVehicleStreamIn(vehicle = vehicle, forPlayer = forPlayer) }
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val onVehicleStreamInListener = mockk<OnVehicleStreamInListener>(relaxed = true)
            callbackListenerManager.register(onVehicleStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleStreamIn(vehicleid = 500, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onVehicleStreamInListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun givenInvalidForPlayerIdItShouldThrowAndCatchException() {
            val onVehicleStreamInListener = mockk<OnVehicleStreamInListener>(relaxed = true)
            callbackListenerManager.register(onVehicleStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleStreamIn(vehicleid = vehicleId, forplayerid = 500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onVehicleStreamInListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehicleStreamInListener = mockk<OnVehicleStreamInListener> {
                every { onVehicleStreamIn(any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleStreamIn(vehicleid = vehicleId, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehicleStreamInListener.onVehicleStreamIn(vehicle = vehicle, forPlayer = forPlayer) }
        }

    }

    @Nested
    inner class OnVehicleStreamOutTests {

        private val vehicle = mockk<Vehicle>()
        private val vehicleId = 69
        private lateinit var forPlayer: Player
        private val forPlayerId = 150

        @BeforeEach
        fun setUp() {
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            forPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(forPlayerId))
        }

        @Test
        fun shouldCallOnVehicleStreamOut() {
            val onVehicleStreamOutListener = mockk<OnVehicleStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onVehicleStreamOutListener)

            callbackProcessor.onVehicleStreamOut(vehicleid = vehicleId, forplayerid = forPlayerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleStreamOutListener.onVehicleStreamOut(vehicle = vehicle, forPlayer = forPlayer) }
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val onVehicleStreamOutListener = mockk<OnVehicleStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onVehicleStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleStreamOut(vehicleid = 500, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onVehicleStreamOutListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun givenInvalidForPlayerIdItShouldThrowAndCatchException() {
            val onVehicleStreamOutListener = mockk<OnVehicleStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onVehicleStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleStreamOut(vehicleid = vehicleId, forplayerid = 500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onVehicleStreamOutListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehicleStreamOutListener = mockk<OnVehicleStreamOutListener> {
                every { onVehicleStreamOut(any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onVehicleStreamOut(vehicleid = vehicleId, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehicleStreamOutListener.onVehicleStreamOut(vehicle = vehicle, forPlayer = forPlayer) }
        }

    }

    @Nested
    inner class OnActorStreamInTests {

        private val actor = mockk<Actor>()
        private val actorId = 69
        private lateinit var forPlayer: Player
        private val forPlayerId = 150

        @BeforeEach
        fun setUp() {
            every { actor.id } returns ActorId.valueOf(actorId)
            server.injector.getInstance<ActorRegistry>().register(actor)
            forPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(forPlayerId))
        }

        @Test
        fun shouldCallOnActorStreamIn() {
            val onActorStreamInListener = mockk<OnActorStreamInListener>(relaxed = true)
            callbackListenerManager.register(onActorStreamInListener)

            callbackProcessor.onActorStreamIn(actorid = actorId, forplayerid = forPlayerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onActorStreamInListener.onActorStreamIn(actor = actor, forPlayer = forPlayer) }
        }

        @Test
        fun givenInvalidActorIdItShouldThrowAndCatchException() {
            val onActorStreamInListener = mockk<OnActorStreamInListener>(relaxed = true)
            callbackListenerManager.register(onActorStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onActorStreamIn(actorid = 500, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onActorStreamInListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid actor ID 500")
        }

        @Test
        fun givenInvalidForPlayerIdItShouldThrowAndCatchException() {
            val onActorStreamInListener = mockk<OnActorStreamInListener>(relaxed = true)
            callbackListenerManager.register(onActorStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onActorStreamIn(actorid = actorId, forplayerid = 500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onActorStreamInListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onActorStreamInListener = mockk<OnActorStreamInListener> {
                every { onActorStreamIn(any(), any()) } throws exception
            }
            callbackListenerManager.register(onActorStreamInListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onActorStreamIn(actorid = actorId, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onActorStreamInListener.onActorStreamIn(actor = actor, forPlayer = forPlayer) }
        }

    }

    @Nested
    inner class OnActorStreamOutTests {

        private val actor = mockk<Actor>()
        private val actorId = 69
        private lateinit var forPlayer: Player
        private val forPlayerId = 150

        @BeforeEach
        fun setUp() {
            every { actor.id } returns ActorId.valueOf(actorId)
            server.injector.getInstance<ActorRegistry>().register(actor)
            forPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(forPlayerId))
        }

        @Test
        fun shouldCallOnActorStreamOut() {
            val onActorStreamOutListener = mockk<OnActorStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onActorStreamOutListener)

            callbackProcessor.onActorStreamOut(actorid = actorId, forplayerid = forPlayerId)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onActorStreamOutListener.onActorStreamOut(actor = actor, forPlayer = forPlayer) }
        }

        @Test
        fun givenInvalidActorIdItShouldThrowAndCatchException() {
            val onActorStreamOutListener = mockk<OnActorStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onActorStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onActorStreamOut(actorid = 500, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onActorStreamOutListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid actor ID 500")
        }

        @Test
        fun givenInvalidForPlayerIdItShouldThrowAndCatchException() {
            val onActorStreamOutListener = mockk<OnActorStreamOutListener>(relaxed = true)
            callbackListenerManager.register(onActorStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onActorStreamOut(actorid = actorId, forplayerid = 500)
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onActorStreamOutListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onActorStreamOutListener = mockk<OnActorStreamOutListener> {
                every { onActorStreamOut(any(), any()) } throws exception
            }
            callbackListenerManager.register(onActorStreamOutListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onActorStreamOut(actorid = actorId, forplayerid = forPlayerId)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onActorStreamOutListener.onActorStreamOut(actor = actor, forPlayer = forPlayer) }
        }

    }

    @Nested
    inner class OnDialogResponseTests {

        private lateinit var player: Player
        private val playerId = 69
        private lateinit var dialog: AbstractDialog
        private var dialogId: Int = 0

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
            dialog = server.injector.getInstance<DialogRegistry>().register { dialogId ->
                this.dialogId = dialogId.value
                mockk(relaxed = true)
            }
        }

        @ParameterizedTest
        @EnumSource(DialogResponse::class)
        fun shouldCallOnDialogResponseAndReturnTrue(response: DialogResponse) {
            val onDialogResponseListener = mockk<OnDialogResponseListener> {
                every { onDialogResponse(any(), any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Processed
            }
            callbackListenerManager.register(onDialogResponseListener)

            val result = callbackProcessor.onDialogResponse(
                    playerid = playerId,
                    dialogid = dialogId,
                    response = response.value,
                    listitem = 69,
                    inputtext = "Hi there"
            )

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onDialogResponseListener.onDialogResponse(
                        player = player,
                        dialogId = DialogId.valueOf(dialogId),
                        response = response,
                        listItem = 69,
                        inputText = "Hi there"
                )
            }
        }

        @Test
        fun shouldCallOnDialogResponseAndReturnFalse() {
            val onDialogResponseListener = mockk<OnDialogResponseListener> {
                every { onDialogResponse(any(), any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Ignored
            }
            callbackListenerManager.register(onDialogResponseListener)

            val result = callbackProcessor.onDialogResponse(
                    playerid = playerId,
                    dialogid = dialogId,
                    response = DialogResponse.LEFT_BUTTON.value,
                    listitem = 69,
                    inputtext = "Hi there"
            )

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onDialogResponseListener.onDialogResponse(
                        player = player,
                        dialogId = DialogId.valueOf(dialogId),
                        response = DialogResponse.LEFT_BUTTON,
                        listItem = 69,
                        inputText = "Hi there"
                )
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onDialogResponseListener = mockk<OnDialogResponseListener>(relaxed = true)
            callbackListenerManager.register(onDialogResponseListener)

            val result = callbackProcessor.onDialogResponse(
                    playerid = 500,
                    dialogid = dialogId,
                    response = DialogResponse.LEFT_BUTTON.value,
                    listitem = 69,
                    inputtext = "Hi there"
            )

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onDialogResponseListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onDialogResponseListener = mockk<OnDialogResponseListener> {
                every { onDialogResponse(any(), any(), any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onDialogResponseListener)

            val result = callbackProcessor.onDialogResponse(
                    playerid = playerId,
                    dialogid = dialogId,
                    response = DialogResponse.LEFT_BUTTON.value,
                    listitem = 69,
                    inputtext = "Hi there"
            )

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify {
                onDialogResponseListener.onDialogResponse(
                        player = player,
                        dialogId = DialogId.valueOf(dialogId),
                        response = DialogResponse.LEFT_BUTTON,
                        listItem = 69,
                        inputText = "Hi there"
                )
            }
        }

    }

    @Nested
    inner class OnPlayerTakeDamageTests {

        private lateinit var player: Player
        private val playerId = 69
        private lateinit var issuer: Player
        private val issuerId = 150

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
            issuer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(issuerId))
        }

        @Test
        fun shouldCallOnPlayerTakeDamage() {
            val onPlayerTakeDamageListener = mockk<OnPlayerTakeDamageListener>(relaxed = true)
            callbackListenerManager.register(onPlayerTakeDamageListener)

            callbackProcessor.onPlayerTakeDamage(
                    playerid = playerId,
                    issuerid = issuerId,
                    amount = 13.37f,
                    weaponid = WeaponModel.TEC9.value,
                    bodypart = BodyPart.GROIN.value
            )

            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerTakeDamageListener.onPlayerTakeDamage(
                        player = player,
                        issuer = issuer,
                        amount = 13.37f,
                        weaponModel = WeaponModel.TEC9,
                        bodyPart = BodyPart.GROIN
                )
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerTakeDamageListener = mockk<OnPlayerTakeDamageListener>(relaxed = true)
            callbackListenerManager.register(onPlayerTakeDamageListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerTakeDamage(
                        playerid = 500,
                        issuerid = issuerId,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerTakeDamageListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidIssuerIdItShouldCallOnPlayerTakeDamage() {
            val onPlayerTakeDamageListener = mockk<OnPlayerTakeDamageListener>(relaxed = true)
            callbackListenerManager.register(onPlayerTakeDamageListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerTakeDamage(
                        playerid = playerId,
                        issuerid = SAMPConstants.INVALID_PLAYER_ID,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerTakeDamageListener.onPlayerTakeDamage(
                        player = player,
                        issuer = null,
                        amount = 13.37f,
                        weaponModel = WeaponModel.TEC9,
                        bodyPart = BodyPart.GROIN
                )
            }
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerTakeDamageListener = mockk<OnPlayerTakeDamageListener> {
                every { onPlayerTakeDamage(any(), any(), any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerTakeDamageListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerTakeDamage(
                        playerid = playerId,
                        issuerid = issuerId,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify {
                onPlayerTakeDamageListener.onPlayerTakeDamage(
                        player = player,
                        issuer = issuer,
                        amount = 13.37f,
                        weaponModel = WeaponModel.TEC9,
                        bodyPart = BodyPart.GROIN
                )
            }
        }

    }

    @Nested
    inner class OnPlayerGiveDamageTests {

        private lateinit var player: Player
        private val playerId = 69
        private lateinit var damagedPlayer: Player
        private val damagedPlayerId = 150

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
            damagedPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(damagedPlayerId))
        }

        @Test
        fun shouldCallOnPlayerGiveDamage() {
            val onPlayerGiveDamageListener = mockk<OnPlayerGiveDamageListener>(relaxed = true)
            callbackListenerManager.register(onPlayerGiveDamageListener)

            callbackProcessor.onPlayerGiveDamage(
                    playerid = playerId,
                    damagedid = damagedPlayerId,
                    amount = 13.37f,
                    weaponid = WeaponModel.TEC9.value,
                    bodypart = BodyPart.GROIN.value
            )

            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerGiveDamageListener.onPlayerGiveDamage(
                        player = player,
                        damagedPlayer = damagedPlayer,
                        amount = 13.37f,
                        weaponModel = WeaponModel.TEC9,
                        bodyPart = BodyPart.GROIN
                )
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerGiveDamageListener = mockk<OnPlayerGiveDamageListener>(relaxed = true)
            callbackListenerManager.register(onPlayerGiveDamageListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerGiveDamage(
                        playerid = 500,
                        damagedid = damagedPlayerId,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerGiveDamageListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidDamagedIdItShouldCallOnPlayerGiveDamage() {
            val onPlayerGiveDamageListener = mockk<OnPlayerGiveDamageListener>(relaxed = true)
            callbackListenerManager.register(onPlayerGiveDamageListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerGiveDamage(
                        playerid = playerId,
                        damagedid = 500,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerGiveDamageListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerGiveDamageListener = mockk<OnPlayerGiveDamageListener> {
                every { onPlayerGiveDamage(any(), any(), any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerGiveDamageListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerGiveDamage(
                        playerid = playerId,
                        damagedid = damagedPlayerId,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify {
                onPlayerGiveDamageListener.onPlayerGiveDamage(
                        player = player,
                        damagedPlayer = damagedPlayer,
                        amount = 13.37f,
                        weaponModel = WeaponModel.TEC9,
                        bodyPart = BodyPart.GROIN
                )
            }
        }

    }

    @Nested
    inner class OnPlayerGiveDamageActorTests {

        private lateinit var player: Player
        private val playerId = 69
        private val actor = mockk<Actor>()
        private val actorId = 150

        @BeforeEach
        fun setUp() {
            every { actor.id } returns ActorId.valueOf(actorId)
            server.injector.getInstance<ActorRegistry>().register(actor)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerGiveDamageActor() {
            val onPlayerGiveDamageActorListener = mockk<OnPlayerGiveDamageActorListener>(relaxed = true)
            callbackListenerManager.register(onPlayerGiveDamageActorListener)

            callbackProcessor.onPlayerGiveDamageActor(
                    playerid = playerId,
                    damaged_actorid = actorId,
                    amount = 13.37f,
                    weaponid = WeaponModel.TEC9.value,
                    bodypart = BodyPart.GROIN.value
            )

            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerGiveDamageActorListener.onPlayerGiveDamageActor(
                        player = player,
                        actor = actor,
                        amount = 13.37f,
                        weaponModel = WeaponModel.TEC9,
                        bodyPart = BodyPart.GROIN
                )
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerGiveDamageActorListener = mockk<OnPlayerGiveDamageActorListener>(relaxed = true)
            callbackListenerManager.register(onPlayerGiveDamageActorListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerGiveDamageActor(
                        playerid = 500,
                        damaged_actorid = actorId,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerGiveDamageActorListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidActorIdItShouldCallOnPlayerGiveDamageActor() {
            val onPlayerGiveDamageActorListener = mockk<OnPlayerGiveDamageActorListener>(relaxed = true)
            callbackListenerManager.register(onPlayerGiveDamageActorListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerGiveDamageActor(
                        playerid = playerId,
                        damaged_actorid = 500,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerGiveDamageActorListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid actor ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerGiveDamageActorListener = mockk<OnPlayerGiveDamageActorListener> {
                every { onPlayerGiveDamageActor(any(), any(), any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerGiveDamageActorListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerGiveDamageActor(
                        playerid = playerId,
                        damaged_actorid = actorId,
                        amount = 13.37f,
                        weaponid = WeaponModel.TEC9.value,
                        bodypart = BodyPart.GROIN.value
                )
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify {
                onPlayerGiveDamageActorListener.onPlayerGiveDamageActor(
                        player = player,
                        actor = actor,
                        amount = 13.37f,
                        weaponModel = WeaponModel.TEC9,
                        bodyPart = BodyPart.GROIN
                )
            }
        }

    }

    @Nested
    inner class OnPlayerClickMapTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerClickMapAndReturnTrue() {
            val onPlayerClickMapListener = mockk<OnPlayerClickMapListener> {
                every { onPlayerClickMap(any(), any()) } returns OnPlayerClickMapListener.Result.Processed
            }
            callbackListenerManager.register(onPlayerClickMapListener)

            val result = callbackProcessor.onPlayerClickMap(playerId, 1f, 2f, 3f)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerClickMapListener.onPlayerClickMap(player, vector3DOf(x = 1f, y = 2f, z = 3f)) }
        }

        @Test
        fun shouldCallOnPlayerClickMapAndReturnFalse() {
            val onPlayerClickMapListener = mockk<OnPlayerClickMapListener> {
                every { onPlayerClickMap(any(), any()) } returns OnPlayerClickMapListener.Result.Continue
            }
            callbackListenerManager.register(onPlayerClickMapListener)

            val result = callbackProcessor.onPlayerClickMap(playerId, 1f, 2f, 3f)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerClickMapListener.onPlayerClickMap(player, vector3DOf(x = 1f, y = 2f, z = 3f)) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerClickMapListener = mockk<OnPlayerClickMapListener>(relaxed = true)
            callbackListenerManager.register(onPlayerClickMapListener)

            val result = callbackProcessor.onPlayerClickMap(500, 1f, 2f, 3f)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerClickMapListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerClickMapListener = mockk<OnPlayerClickMapListener> {
                every { onPlayerClickMap(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerClickMapListener)

            val result = callbackProcessor.onPlayerClickMap(playerId, 1f, 2f, 3f)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerClickMapListener.onPlayerClickMap(player, vector3DOf(x = 1f, y = 2f, z = 3f)) }
        }

    }

    @Nested
    inner class OnPlayerClickTextDrawTests {

        private lateinit var player: Player
        private val playerId = 69
        private val textDraw = mockk<TextDraw>()
        private val textDrawId = 150

        @BeforeEach
        fun setUp() {
            every { textDraw.id } returns TextDrawId.valueOf(textDrawId)
            every { textDraw.onClick(any<Player>()) } returns OnPlayerClickTextDrawListener.Result.NotFound
            server.injector.getInstance<TextDrawRegistry>().register(textDraw)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnPlayerClickTextDrawAndReturnTrue() {
            val onPlayerClickTextDrawListener = mockk<OnPlayerClickTextDrawListener> {
                every { onPlayerClickTextDraw(any(), any()) } returns OnPlayerClickTextDrawListener.Result.Processed
            }
            callbackListenerManager.register(onPlayerClickTextDrawListener)

            val result = callbackProcessor.onPlayerClickTextDraw(playerId, textDrawId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerClickTextDrawListener.onPlayerClickTextDraw(player, textDraw) }
        }

        @Test
        fun shouldCallOnPlayerClickTextDrawAndReturnFalse() {
            val onPlayerClickTextDrawListener = mockk<OnPlayerClickTextDrawListener> {
                every { onPlayerClickTextDraw(any(), any()) } returns OnPlayerClickTextDrawListener.Result.NotFound
            }
            callbackListenerManager.register(onPlayerClickTextDrawListener)

            val result = callbackProcessor.onPlayerClickTextDraw(playerId, textDrawId)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerClickTextDrawListener.onPlayerClickTextDraw(player, textDraw) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerClickTextDrawListener = mockk<OnPlayerClickTextDrawListener>(relaxed = true)
            callbackListenerManager.register(onPlayerClickTextDrawListener)

            val result = callbackProcessor.onPlayerClickTextDraw(500, textDrawId)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerClickTextDrawListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidPlayerTextDrawIdItShouldThrowAndCatchException() {
            val onPlayerClickTextDrawListener = mockk<OnPlayerClickTextDrawListener>(relaxed = true)
            callbackListenerManager.register(onPlayerClickTextDrawListener)

            val result = callbackProcessor.onPlayerClickTextDraw(playerId, 500)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerClickTextDrawListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid text draw ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerClickTextDrawListener = mockk<OnPlayerClickTextDrawListener> {
                every { onPlayerClickTextDraw(any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerClickTextDrawListener)

            val result = callbackProcessor.onPlayerClickTextDraw(playerId, textDrawId)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerClickTextDrawListener.onPlayerClickTextDraw(player, textDraw) }
        }

    }

    @Nested
    inner class OnPlayerClickPlayerTextDrawTests {

        private lateinit var player: Player
        private val playerId = 69
        private val playerTextDraw = mockk<PlayerTextDraw>()
        private val playerTextDrawId = 150

        @BeforeEach
        fun setUp() {
            every { playerTextDraw.id } returns PlayerTextDrawId.valueOf(playerTextDrawId)
            every { playerTextDraw.onClick() } returns OnPlayerClickPlayerTextDrawListener.Result.NotFound
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
            player.playerTextDrawRegistry.register(playerTextDraw)
        }

        @Test
        fun shouldCallOnPlayerClickPlayerTextDrawAndReturnTrue() {
            val onPlayerClickPlayerTextDrawListener = mockk<OnPlayerClickPlayerTextDrawListener> {
                every { onPlayerClickPlayerTextDraw(any()) } returns OnPlayerClickPlayerTextDrawListener.Result.Processed
            }
            callbackListenerManager.register(onPlayerClickPlayerTextDrawListener)

            val result = callbackProcessor.onPlayerClickPlayerTextDraw(playerId, playerTextDrawId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerClickPlayerTextDrawListener.onPlayerClickPlayerTextDraw(playerTextDraw) }
        }

        @Test
        fun shouldCallOnPlayerClickPlayerTextDrawAndReturnFalse() {
            val onPlayerClickPlayerTextDrawListener = mockk<OnPlayerClickPlayerTextDrawListener> {
                every { onPlayerClickPlayerTextDraw(any()) } returns OnPlayerClickPlayerTextDrawListener.Result.NotFound
            }
            callbackListenerManager.register(onPlayerClickPlayerTextDrawListener)

            val result = callbackProcessor.onPlayerClickPlayerTextDraw(playerId, playerTextDrawId)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onPlayerClickPlayerTextDrawListener.onPlayerClickPlayerTextDraw(playerTextDraw) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerClickPlayerTextDrawListener = mockk<OnPlayerClickPlayerTextDrawListener>(relaxed = true)
            callbackListenerManager.register(onPlayerClickPlayerTextDrawListener)

            val result = callbackProcessor.onPlayerClickPlayerTextDraw(500, playerTextDrawId)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerClickPlayerTextDrawListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidPlayerTextDrawIdItShouldThrowAndCatchException() {
            val onPlayerClickPlayerTextDrawListener = mockk<OnPlayerClickPlayerTextDrawListener>(relaxed = true)
            callbackListenerManager.register(onPlayerClickPlayerTextDrawListener)

            val result = callbackProcessor.onPlayerClickPlayerTextDraw(playerId, 500)

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerClickPlayerTextDrawListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player text draw ID 500 for player ID 69")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerClickPlayerTextDrawListener = mockk<OnPlayerClickPlayerTextDrawListener> {
                every { onPlayerClickPlayerTextDraw(any()) } throws exception
            }
            callbackListenerManager.register(onPlayerClickPlayerTextDrawListener)

            val result = callbackProcessor.onPlayerClickPlayerTextDraw(playerId, playerTextDrawId)

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onPlayerClickPlayerTextDrawListener.onPlayerClickPlayerTextDraw(playerTextDraw) }
        }

    }

    @Nested
    inner class OnIncomingConnectionTests {

        private val playerId = 69

        @Test
        fun shouldCallOnIncomingConnection() {
            val onIncomingConnectionListener = mockk<OnIncomingConnectionListener>(relaxed = true)
            callbackListenerManager.register(onIncomingConnectionListener)

            callbackProcessor.onIncomingConnection(playerId, "127.0.0.1", 7777)

            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onIncomingConnectionListener.onIncomingConnection(PlayerId.valueOf(playerId), "127.0.0.1", 7777) }
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onIncomingConnectionListener = mockk<OnIncomingConnectionListener> {
                every { onIncomingConnection(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onIncomingConnectionListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onIncomingConnection(playerId, "127.0.0.1", 7777)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onIncomingConnectionListener.onIncomingConnection(PlayerId.valueOf(playerId), "127.0.0.1", 7777) }
        }

    }

    @Nested
    inner class OnTrailerUpdateTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.onExit(any<Player>()) } just Runs
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Test
        fun shouldCallOnTrailerUpdate() {
            val onTrailerUpdateListener = mockk<OnTrailerUpdateListener>(relaxed = true)
            callbackListenerManager.register(onTrailerUpdateListener)

            val result = callbackProcessor.onTrailerUpdate(playerId, vehicleId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onTrailerUpdateListener.onTrailerUpdate(player, vehicle) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onTrailerUpdateListener = mockk<OnTrailerUpdateListener>(relaxed = true)
            callbackListenerManager.register(onTrailerUpdateListener)

            val result = callbackProcessor.onTrailerUpdate(500, vehicleId)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onTrailerUpdateListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onTrailerUpdateListener = mockk<OnTrailerUpdateListener> {
                every { onTrailerUpdate(any(), any()) } throws exception
            }
            callbackListenerManager.register(onTrailerUpdateListener)

            val result = callbackProcessor.onTrailerUpdate(playerId, 500)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onTrailerUpdateListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onTrailerUpdateListener = mockk<OnTrailerUpdateListener> {
                every { onTrailerUpdate(any(), any()) } throws exception
            }
            callbackListenerManager.register(onTrailerUpdateListener)

            val result = callbackProcessor.onTrailerUpdate(playerId, vehicleId)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onTrailerUpdateListener.onTrailerUpdate(player, vehicle) }
        }

    }

    @Nested
    inner class OnVehicleSirenStateChangeTests {

        private lateinit var player: Player
        private val playerId = 69
        private val vehicleId = 127
        private val vehicle: Vehicle = mockk()

        @BeforeEach
        fun setUp() {
            every { vehicle.onExit(any<Player>()) } just Runs
            every { vehicle.id } returns VehicleId.valueOf(vehicleId)
            server.injector.getInstance<VehicleRegistry>().register(vehicle)
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @ParameterizedTest
        @EnumSource(VehicleSirenState::class)
        fun shouldCallOnVehicleSirenStateChange(sirenState: VehicleSirenState) {
            val onVehicleSirenStateChangeListener = mockk<OnVehicleSirenStateChangeListener>(relaxed = true)
            callbackListenerManager.register(onVehicleSirenStateChangeListener)

            val result = callbackProcessor.onVehicleSirenStateChange(playerId, vehicleId, sirenState.value)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify { onVehicleSirenStateChangeListener.onVehicleSirenStateChange(player, vehicle, sirenState) }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onVehicleSirenStateChangeListener = mockk<OnVehicleSirenStateChangeListener>(relaxed = true)
            callbackListenerManager.register(onVehicleSirenStateChangeListener)

            val result = callbackProcessor.onVehicleSirenStateChange(500, vehicleId, VehicleSirenState.ON.value)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onVehicleSirenStateChangeListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidVehicleIdItShouldThrowAndCatchException() {
            val exception = RuntimeException("test")
            val onVehicleSirenStateChangeListener = mockk<OnVehicleSirenStateChangeListener> {
                every { onVehicleSirenStateChange(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleSirenStateChangeListener)

            val result = callbackProcessor.onVehicleSirenStateChange(playerId, 500, VehicleSirenState.ON.value)

            assertThat(result)
                    .isTrue()
            val slot = slot<Exception>()
            verify { onVehicleSirenStateChangeListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid vehicle ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onVehicleSirenStateChangeListener = mockk<OnVehicleSirenStateChangeListener> {
                every { onVehicleSirenStateChange(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onVehicleSirenStateChangeListener)

            val result = callbackProcessor.onVehicleSirenStateChange(playerId, vehicleId, VehicleSirenState.ON.value)

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify { onVehicleSirenStateChangeListener.onVehicleSirenStateChange(player, vehicle, VehicleSirenState.ON) }
        }

    }

    @Nested
    inner class OnPlayerClickPlayerTests {

        private lateinit var player: Player
        private val playerId = 69
        private lateinit var clickedPlayer: Player
        private val clickedPlayerId = 187

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
            clickedPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(clickedPlayerId))
        }

        @Test
        fun shouldCallOnPlayerClickPlayerAndReturnTrue() {
            val onPlayerClickPlayerListener = mockk<OnPlayerClickPlayerListener> {
                every { onPlayerClickPlayer(any(), any(), any()) } returns OnPlayerClickPlayerListener.Result.Processed
            }
            callbackListenerManager.register(onPlayerClickPlayerListener)

            val result = callbackProcessor.onPlayerClickPlayer(
                    playerid = playerId,
                    clickedplayerid = clickedPlayerId,
                    source = ClickPlayerSource.SCOREBOARD.value
            )

            assertThat(result)
                    .isTrue()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerClickPlayerListener.onPlayerClickPlayer(
                        player = player,
                        clickedPlayer = clickedPlayer,
                        source = ClickPlayerSource.SCOREBOARD
                )
            }
        }

        @Test
        fun shouldCallOnPlayerClickPlayerAndReturnFalse() {
            val onPlayerClickPlayerListener = mockk<OnPlayerClickPlayerListener> {
                every { onPlayerClickPlayer(any(), any(), any()) } returns OnPlayerClickPlayerListener.Result.Continue
            }
            callbackListenerManager.register(onPlayerClickPlayerListener)

            val result = callbackProcessor.onPlayerClickPlayer(
                    playerid = playerId,
                    clickedplayerid = clickedPlayerId,
                    source = ClickPlayerSource.SCOREBOARD.value
            )

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerClickPlayerListener.onPlayerClickPlayer(
                        player = player,
                        clickedPlayer = clickedPlayer,
                        source = ClickPlayerSource.SCOREBOARD
                )
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerClickPlayerListener = mockk<OnPlayerClickPlayerListener>(relaxed = true)
            callbackListenerManager.register(onPlayerClickPlayerListener)

            val result = callbackProcessor.onPlayerClickPlayer(
                    playerid = 500,
                    clickedplayerid = clickedPlayerId,
                    source = ClickPlayerSource.SCOREBOARD.value
            )

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerClickPlayerListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun givenInvalidClickedPlayerIdItShouldThrowAndCatchException() {
            val onPlayerClickPlayerListener = mockk<OnPlayerClickPlayerListener>(relaxed = true)
            callbackListenerManager.register(onPlayerClickPlayerListener)

            val result = callbackProcessor.onPlayerClickPlayer(
                    playerid = playerId,
                    clickedplayerid = 500,
                    source = ClickPlayerSource.SCOREBOARD.value
            )

            assertThat(result)
                    .isFalse()
            val slot = slot<Exception>()
            verify { onPlayerClickPlayerListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerClickPlayerListener = mockk<OnPlayerClickPlayerListener> {
                every { onPlayerClickPlayer(any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerClickPlayerListener)

            val result = callbackProcessor.onPlayerClickPlayer(
                    playerid = playerId,
                    clickedplayerid = clickedPlayerId,
                    source = ClickPlayerSource.SCOREBOARD.value
            )

            assertThat(result)
                    .isFalse()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify {
                onPlayerClickPlayerListener.onPlayerClickPlayer(
                        player = player,
                        clickedPlayer = clickedPlayer,
                        source = ClickPlayerSource.SCOREBOARD
                )
            }
        }

    }

    @Nested
    inner class OnPlayerEditObjectTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Nested
        inner class MapObjectTests {

            private val mapObject = mockk<MapObject>()
            private val mapObjectId = 187

            @BeforeEach
            fun setUp() {
                every { mapObject.id } returns MapObjectId.valueOf(mapObjectId)
                every { mapObject.onEdit(any(), any(), any(), any()) } just Runs
                server.injector.getInstance<MapObjectRegistry>().register(mapObject)
            }

            @ParameterizedTest
            @EnumSource(ObjectEditResponse::class)
            fun shouldCallOnPlayerEditMapObject(response: ObjectEditResponse) {
                val onPlayerEditObjectListener = mockk<OnPlayerEditMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerEditObjectListener)

                callbackProcessor.onPlayerEditObject(
                        playerid = playerId,
                        playerobject = false,
                        objectid = mapObjectId,
                        response = response.value,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f,
                        fRotX = 4f,
                        fRotY = 5f,
                        fRotZ = 6f
                )

                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerEditObjectListener.onPlayerEditMapObject(
                            player = player,
                            mapObject = mapObject,
                            response = response,
                            offset = vector3DOf(1f, 2f, 3f),
                            rotation = vector3DOf(4f, 5f, 6f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerEditObjectListener = mockk<OnPlayerEditMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerEditObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerEditObject(
                            playerid = 500,
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
                }

                assertThat(caughtThrowable)
                        .isNull()
                val slot = slot<Exception>()
                verify { onPlayerEditObjectListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun givenInvalidMapObjectIdItShouldThrowAndCatchException() {
                val onPlayerEditObjectListener = mockk<OnPlayerEditMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerEditObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerEditObject(
                            playerid = playerId,
                            playerobject = false,
                            objectid = 500,
                            response = ObjectEditResponse.FINAL.value,
                            fX = 1f,
                            fY = 2f,
                            fZ = 3f,
                            fRotX = 4f,
                            fRotY = 5f,
                            fRotZ = 6f
                    )
                }

                assertThat(caughtThrowable)
                        .isNull()
                val slot = slot<Exception>()
                verify { onPlayerEditObjectListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid map object ID 500")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerEditObjectListener = mockk<OnPlayerEditMapObjectListener> {
                    every { onPlayerEditMapObject(any(), any(), any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerEditObjectListener)

                val caughtThrowable = catchThrowable {
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
                }

                assertThat(caughtThrowable)
                        .isNull()
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerEditObjectListener.onPlayerEditMapObject(
                            player = player,
                            mapObject = mapObject,
                            response = ObjectEditResponse.FINAL,
                            offset = vector3DOf(1f, 2f, 3f),
                            rotation = vector3DOf(4f, 5f, 6f)
                    )
                }
            }
        }

        @Nested
        inner class PlayerMapObjectTests {

            private val playerMapObject = mockk<PlayerMapObject>()
            private val playerMapObjectId = 187

            @BeforeEach
            fun setUp() {
                every { playerMapObject.id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
                every { playerMapObject.onEdit(any(), any(), any()) } just Runs
                player.playerMapObjectRegistry.register(playerMapObject)
            }

            @ParameterizedTest
            @EnumSource(ObjectEditResponse::class)
            fun shouldCallOnPlayerEditPlayerMapObject(response: ObjectEditResponse) {
                val onPlayerEditObjectListener = mockk<OnPlayerEditPlayerMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerEditObjectListener)

                callbackProcessor.onPlayerEditObject(
                        playerid = playerId,
                        playerobject = true,
                        objectid = playerMapObjectId,
                        response = response.value,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f,
                        fRotX = 4f,
                        fRotY = 5f,
                        fRotZ = 6f
                )

                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerEditObjectListener.onPlayerEditPlayerMapObject(
                            playerMapObject = playerMapObject,
                            response = response,
                            offset = vector3DOf(1f, 2f, 3f),
                            rotation = vector3DOf(4f, 5f, 6f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerEditObjectListener = mockk<OnPlayerEditPlayerMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerEditObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerEditObject(
                            playerid = 500,
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
                }

                assertThat(caughtThrowable)
                        .isNull()
                val slot = slot<Exception>()
                verify { onPlayerEditObjectListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun givenInvalidPlayerMapObjectIdItShouldThrowAndCatchException() {
                val onPlayerEditObjectListener = mockk<OnPlayerEditPlayerMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerEditObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerEditObject(
                            playerid = playerId,
                            playerobject = true,
                            objectid = 500,
                            response = ObjectEditResponse.FINAL.value,
                            fX = 1f,
                            fY = 2f,
                            fZ = 3f,
                            fRotX = 4f,
                            fRotY = 5f,
                            fRotZ = 6f
                    )
                }

                assertThat(caughtThrowable)
                        .isNull()
                val slot = slot<Exception>()
                verify { onPlayerEditObjectListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player map object ID 500 for player ID 69")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerEditObjectListener = mockk<OnPlayerEditPlayerMapObjectListener> {
                    every { onPlayerEditPlayerMapObject(any(), any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerEditObjectListener)

                val caughtThrowable = catchThrowable {
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
                }

                assertThat(caughtThrowable)
                        .isNull()
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerEditObjectListener.onPlayerEditPlayerMapObject(
                            playerMapObject = playerMapObject,
                            response = ObjectEditResponse.FINAL,
                            offset = vector3DOf(1f, 2f, 3f),
                            rotation = vector3DOf(4f, 5f, 6f)
                    )
                }
            }
        }

    }

    @Nested
    inner class OnPlayerEditAttachedObjectTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @ParameterizedTest
        @EnumSource(AttachedObjectEditResponse::class)
        fun shouldCallOnPlayerEditAttachedObject(response: AttachedObjectEditResponse) {
            val onPlayerEditAttachedObjectListener = mockk<OnPlayerEditAttachedObjectListener>(relaxed = true)
            callbackListenerManager.register(onPlayerEditAttachedObjectListener)

            callbackProcessor.onPlayerEditAttachedObject(
                    playerid = playerId,
                    response = response.value,
                    index = 3,
                    modelid = 1337,
                    boneid = Bone.CALF_LEFT.value,
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

            verify { uncaughtExceptionNotifier wasNot Called }
            verify {
                onPlayerEditAttachedObjectListener.onPlayerEditAttachedObject(
                        player = player,
                        response = response,
                        slot = player.attachedObjectSlots[3],
                        modelId = 1337,
                        bone = Bone.CALF_LEFT,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f),
                        scale = vector3DOf(7f, 8f, 9f)
                )
            }
        }

        @Test
        fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
            val onPlayerEditAttachedObjectListener = mockk<OnPlayerEditAttachedObjectListener>(relaxed = true)
            callbackListenerManager.register(onPlayerEditAttachedObjectListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerEditAttachedObject(
                        playerid = 500,
                        response = AttachedObjectEditResponse.SAVE.value,
                        index = 3,
                        modelid = 1337,
                        boneid = Bone.CALF_LEFT.value,
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
            }

            assertThat(caughtThrowable)
                    .isNull()
            val slot = slot<Exception>()
            verify { onPlayerEditAttachedObjectListener wasNot Called }
            verify { uncaughtExceptionNotifier.notify(capture(slot)) }
            assertThat(slot.captured)
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessage("Invalid player ID 500")
        }

        @Test
        fun shouldCatchException() {
            val exception = RuntimeException("test")
            val onPlayerEditAttachedObjectListener = mockk<OnPlayerEditAttachedObjectListener> {
                every { onPlayerEditAttachedObject(any(), any(), any(), any(), any(), any(), any(), any()) } throws exception
            }
            callbackListenerManager.register(onPlayerEditAttachedObjectListener)

            val caughtThrowable = catchThrowable {
                callbackProcessor.onPlayerEditAttachedObject(
                        playerid = playerId,
                        response = AttachedObjectEditResponse.SAVE.value,
                        index = 3,
                        modelid = 1337,
                        boneid = Bone.CALF_LEFT.value,
                        fOffsetX = 1f,
                        fOffsetY = 2f,
                        fOffsetZ = 3f,
                        fRotX = 4f,
                        fRotY = 5f,
                        fRotZ = 6f,
                        fScaleX = 7f,
                        fScaleY = 8f,
                        fScaleZ = 9f)
            }

            assertThat(caughtThrowable)
                    .isNull()
            verify { uncaughtExceptionNotifier.notify(exception) }
            verify {
                onPlayerEditAttachedObjectListener.onPlayerEditAttachedObject(
                        player = player,
                        response = AttachedObjectEditResponse.SAVE,
                        slot = player.attachedObjectSlots[3],
                        modelId = 1337,
                        bone = Bone.CALF_LEFT,
                        offset = vector3DOf(1f, 2f, 3f),
                        rotation = vector3DOf(4f, 5f, 6f),
                        scale = vector3DOf(7f, 8f, 9f)
                )
            }
        }

    }

    @Nested
    inner class OnPlayerSelectObjectTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Nested
        inner class MapObjectTests {

            private val mapObject = mockk<MapObject>()
            private val mapObjectId = 187

            @BeforeEach
            fun setUp() {
                every { mapObject.id } returns MapObjectId.valueOf(mapObjectId)
                every { mapObject.onSelect(any(), any(), any()) } just Runs
                server.injector.getInstance<MapObjectRegistry>().register(mapObject)
            }

            @Test
            fun shouldCallOnPlayerSelectMapObject() {
                val onPlayerSelectObjectListener = mockk<OnPlayerSelectMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerSelectObjectListener)

                callbackProcessor.onPlayerSelectObject(
                        playerid = playerId,
                        type = SAMPConstants.SELECT_OBJECT_GLOBAL_OBJECT,
                        objectid = mapObjectId,
                        modelid = 1337,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerSelectObjectListener.onPlayerSelectMapObject(
                            player = player,
                            mapObject = mapObject,
                            modelId = 1337,
                            coordinates = vector3DOf(1f, 2f, 3f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerSelectObjectListener = mockk<OnPlayerSelectMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerSelectObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerSelectObject(
                            playerid = 500,
                            type = SAMPConstants.SELECT_OBJECT_GLOBAL_OBJECT,
                            objectid = mapObjectId,
                            modelid = 1337,
                            fX = 1f,
                            fY = 2f,
                            fZ = 3f
                    )
                }

                assertThat(caughtThrowable)
                        .isNull()
                val slot = slot<Exception>()
                verify { onPlayerSelectObjectListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun givenInvalidMapObjectIdItShouldThrowAndCatchException() {
                val onPlayerSelectObjectListener = mockk<OnPlayerSelectMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerSelectObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerSelectObject(
                            playerid = playerId,
                            type = SAMPConstants.SELECT_OBJECT_GLOBAL_OBJECT,
                            objectid = 500,
                            modelid = 1337,
                            fX = 1f,
                            fY = 2f,
                            fZ = 3f
                    )
                }

                assertThat(caughtThrowable)
                        .isNull()
                val slot = slot<Exception>()
                verify { onPlayerSelectObjectListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid map object ID 500")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerSelectObjectListener = mockk<OnPlayerSelectMapObjectListener> {
                    every { onPlayerSelectMapObject(any(), any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerSelectObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerSelectObject(
                            playerid = playerId,
                            type = SAMPConstants.SELECT_OBJECT_GLOBAL_OBJECT,
                            objectid = mapObjectId,
                            modelid = 1337,
                            fX = 1f,
                            fY = 2f,
                            fZ = 3f
                    )
                }

                assertThat(caughtThrowable)
                        .isNull()
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerSelectObjectListener.onPlayerSelectMapObject(
                            player = player,
                            mapObject = mapObject,
                            modelId = 1337,
                            coordinates = vector3DOf(1f, 2f, 3f)
                    )
                }
            }
        }

        @Nested
        inner class PlayerMapObjectTests {

            private val playerMapObject = mockk<PlayerMapObject>()
            private val playerMapObjectId = 187

            @BeforeEach
            fun setUp() {
                every { playerMapObject.id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
                every { playerMapObject.onSelect(any(), any()) } just Runs
                player.playerMapObjectRegistry.register(playerMapObject)
            }

            @Test
            fun shouldCallOnPlayerSelectPlayerMapObject() {
                val onPlayerSelectObjectListener = mockk<OnPlayerSelectPlayerMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerSelectObjectListener)

                callbackProcessor.onPlayerSelectObject(
                        playerid = playerId,
                        type = SAMPConstants.SELECT_OBJECT_PLAYER_OBJECT,
                        objectid = playerMapObjectId,
                        modelid = 1337,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerSelectObjectListener.onPlayerSelectPlayerMapObject(
                            playerMapObject = playerMapObject,
                            modelId = 1337,
                            coordinates = vector3DOf(1f, 2f, 3f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerSelectObjectListener = mockk<OnPlayerSelectPlayerMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerSelectObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerSelectObject(
                            playerid = 500,
                            type = SAMPConstants.SELECT_OBJECT_PLAYER_OBJECT,
                            objectid = playerMapObjectId,
                            modelid = 1337,
                            fX = 1f,
                            fY = 2f,
                            fZ = 3f
                    )
                }

                assertThat(caughtThrowable)
                        .isNull()
                val slot = slot<Exception>()
                verify { onPlayerSelectObjectListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun givenInvalidPlayerMapObjectIdItShouldThrowAndCatchException() {
                val onPlayerSelectObjectListener = mockk<OnPlayerSelectPlayerMapObjectListener>(relaxed = true)
                callbackListenerManager.register(onPlayerSelectObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerSelectObject(
                            playerid = playerId,
                            type = SAMPConstants.SELECT_OBJECT_PLAYER_OBJECT,
                            objectid = 500,
                            modelid = 1337,
                            fX = 1f,
                            fY = 2f,
                            fZ = 3f
                    )
                }

                assertThat(caughtThrowable)
                        .isNull()
                val slot = slot<Exception>()
                verify { onPlayerSelectObjectListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player map object ID 500 for player ID 69")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerSelectObjectListener = mockk<OnPlayerSelectPlayerMapObjectListener> {
                    every { onPlayerSelectPlayerMapObject(any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerSelectObjectListener)

                val caughtThrowable = catchThrowable {
                    callbackProcessor.onPlayerSelectObject(
                            playerid = playerId,
                            type = SAMPConstants.SELECT_OBJECT_PLAYER_OBJECT,
                            objectid = playerMapObjectId,
                            modelid = 1337,
                            fX = 1f,
                            fY = 2f,
                            fZ = 3f
                    )
                }

                assertThat(caughtThrowable)
                        .isNull()
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerSelectObjectListener.onPlayerSelectPlayerMapObject(
                            playerMapObject = playerMapObject,
                            modelId = 1337,
                            coordinates = vector3DOf(1f, 2f, 3f)
                    )
                }
            }
        }

    }

    @Nested
    inner class OnPlayerWeaponShotTests {

        private lateinit var player: Player
        private val playerId = 69

        @BeforeEach
        fun setUp() {
            player = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(playerId))
        }

        @Nested
        inner class PlayerTargetTests {

            private lateinit var targetPlayer: Player
            private val targetPlayerId = 187

            @BeforeEach
            fun setUp() {
                targetPlayer = server.injector.getInstance<PlayerFactory>().create(PlayerId.valueOf(targetPlayerId))
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnTrue() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.AllowDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER.value,
                        hitid = targetPlayerId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.PlayerTarget(targetPlayer),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnFalse() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.PreventDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER.value,
                        hitid = targetPlayerId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isFalse()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.PlayerTarget(targetPlayer),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = 500,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER.value,
                        hitid = targetPlayerId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f)

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun givenInvalidTargetPlayerIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER.value,
                        hitid = 500,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER.value,
                        hitid = targetPlayerId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.PlayerTarget(targetPlayer),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }
        }

        @Nested
        inner class VehicleTargetTests {

            private val targetVehicle = mockk<Vehicle>()
            private val targetVehicleId = 187

            @BeforeEach
            fun setUp() {
                every { targetVehicle.id } returns VehicleId.valueOf(targetVehicleId)
                server.injector.getInstance<VehicleRegistry>().register(targetVehicle)
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnTrue() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.AllowDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.VEHICLE.value,
                        hitid = targetVehicleId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.VehicleTarget(targetVehicle),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnFalse() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.PreventDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.VEHICLE.value,
                        hitid = targetVehicleId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isFalse()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.VehicleTarget(targetVehicle),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = 500,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.VEHICLE.value,
                        hitid = targetVehicleId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f)

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun givenInvalidTargetVehicleIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.VEHICLE.value,
                        hitid = 500,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid vehicle ID 500")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.VEHICLE.value,
                        hitid = targetVehicleId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.VehicleTarget(targetVehicle),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }
        }

        @Nested
        inner class MapObjectTargetTests {

            private val targetMapObject = mockk<MapObject>()
            private val targetMapObjectId = 187

            @BeforeEach
            fun setUp() {
                every { targetMapObject.id } returns MapObjectId.valueOf(targetMapObjectId)
                server.injector.getInstance<MapObjectRegistry>().register(targetMapObject)
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnTrue() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.AllowDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.OBJECT.value,
                        hitid = targetMapObjectId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.MapObjectTarget(targetMapObject),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnFalse() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.PreventDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.OBJECT.value,
                        hitid = targetMapObjectId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isFalse()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.MapObjectTarget(targetMapObject),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = 500,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.OBJECT.value,
                        hitid = targetMapObjectId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f)

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun givenInvalidTargetMapObjectIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.OBJECT.value,
                        hitid = 500,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid map object ID 500")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.OBJECT.value,
                        hitid = targetMapObjectId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.MapObjectTarget(targetMapObject),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }
        }

        @Nested
        inner class PlayerMapObjectTargetTests {

            private val targetPlayerMapObject = mockk<PlayerMapObject>()
            private val targetPlayerMapObjectId = 187

            @BeforeEach
            fun setUp() {
                every { targetPlayerMapObject.id } returns PlayerMapObjectId.valueOf(targetPlayerMapObjectId)
                player.playerMapObjectRegistry.register(targetPlayerMapObject)
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnTrue() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.AllowDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER_OBJECT.value,
                        hitid = targetPlayerMapObjectId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.PlayerMapObjectTarget(targetPlayerMapObject),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnFalse() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.PreventDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER_OBJECT.value,
                        hitid = targetPlayerMapObjectId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isFalse()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.PlayerMapObjectTarget(targetPlayerMapObject),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = 500,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER_OBJECT.value,
                        hitid = targetPlayerMapObjectId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f)

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun givenInvalidTargetPlayerMapObjectIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER_OBJECT.value,
                        hitid = 500,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player map object ID 500 for player ID 69")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = playerId,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.PLAYER_OBJECT.value,
                        hitid = targetPlayerMapObjectId,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f
                )

                assertThat(result)
                        .isTrue()
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.PlayerMapObjectTarget(targetPlayerMapObject),
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }
        }

        @Nested
        inner class NoTargetTests {

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnTrue() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.AllowDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

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
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.NoTarget,
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun shouldCallOnPlayerWeaponShotAndReturnFalse() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } returns OnPlayerWeaponShotListener.Result.PreventDamage
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

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
                        .isFalse()
                verify { uncaughtExceptionNotifier wasNot Called }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.NoTarget,
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }

            @Test
            fun givenInvalidPlayerIdItShouldThrowAndCatchException() {
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener>(relaxed = true)
                callbackListenerManager.register(onPlayerWeaponShotListener)

                val result = callbackProcessor.onPlayerWeaponShot(
                        playerid = 500,
                        weaponid = WeaponModel.AK47.value,
                        hittype = BulletHitType.NONE.value,
                        hitid = 0,
                        fX = 1f,
                        fY = 2f,
                        fZ = 3f)

                assertThat(result)
                        .isTrue()
                val slot = slot<Exception>()
                verify { onPlayerWeaponShotListener wasNot Called }
                verify { uncaughtExceptionNotifier.notify(capture(slot)) }
                assertThat(slot.captured)
                        .isInstanceOf(IllegalArgumentException::class.java)
                        .hasMessage("Invalid player ID 500")
            }

            @Test
            fun shouldCatchException() {
                val exception = RuntimeException("test")
                val onPlayerWeaponShotListener = mockk<OnPlayerWeaponShotListener> {
                    every { onPlayerShotWeapon(any(), any(), any(), any()) } throws exception
                }
                callbackListenerManager.register(onPlayerWeaponShotListener)

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
                verify { uncaughtExceptionNotifier.notify(exception) }
                verify {
                    onPlayerWeaponShotListener.onPlayerShotWeapon(
                            player = player,
                            weaponModel = WeaponModel.AK47,
                            hitTarget = OnPlayerWeaponShotListener.Target.NoTarget,
                            coordinates = vector3DOf(x = 1f, y = 2f, z = 3f)
                    )
                }
            }
        }

    }

    @Suppress("unused")
    @Singleton
    private class LifecycleAwareService {

        var isShutdown: Boolean = false
            private set

        @PreDestroy
        fun shutdown() {
            isShutdown = true
        }

    }

    @Suppress("unused")
    class TestGameMode : GameMode() {

        override fun getPlugins(): List<Plugin> = emptyList()

        override fun getModules(): List<Module> = listOf(TestModule())

    }

    private class TestModule : KampModule() {

        override fun configure() {
            bind(UncaughtExceptionNotifier::class.java).toInstance(mockk())
            bind(UnknownCommandHandler::class.java).toInstance(TestUnknownCommandHandler())
        }

    }

    private class TestUnknownCommandHandler : UnknownCommandHandler {

        override fun handle(player: Player, command: String, parameters: List<String>): OnPlayerCommandTextListener.Result =
                OnPlayerCommandTextListener.Result.UnknownCommand
    }

}