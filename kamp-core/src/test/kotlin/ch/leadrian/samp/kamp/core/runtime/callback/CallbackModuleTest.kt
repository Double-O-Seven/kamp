package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.util.getInstance
import com.google.inject.Guice
import com.google.inject.Injector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.inject.Inject

internal class CallbackModuleTest {

    @Test
    fun shouldCreateInjectorAsSingleton() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(CallbackModule())
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector
        private lateinit var testService: TestService

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(CallbackModule())
            testService = injector.getInstance()
        }

        @Test
        fun shouldInjectCheckpointCallbackListenerAsSingleton() {
            val checkpointCallbackListener = injector.getInstance<CheckpointCallbackListener>()

            assertThat(checkpointCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<CheckpointCallbackListener>())
        }

        @Test
        fun shouldInjectDialogCallbackListenerAsSingleton() {
            val dialogCallbackListener = injector.getInstance<DialogCallbackListener>()

            assertThat(dialogCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<DialogCallbackListener>())
        }

        @Test
        fun shouldInjectMapObjectCallbackListenerAsSingleton() {
            val mapObjectCallbackListener = injector.getInstance<MapObjectCallbackListener>()

            assertThat(mapObjectCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<MapObjectCallbackListener>())
        }

        @Test
        fun shouldInjectMenuCallbackListenerAsSingleton() {
            val menuCallbackListener = injector.getInstance<MenuCallbackListener>()

            assertThat(menuCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<MenuCallbackListener>())
        }

        @Test
        fun shouldInjectPickupCallbackListenerAsSingleton() {
            val pickupCallbackListener = injector.getInstance<PickupCallbackListener>()

            assertThat(pickupCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<PickupCallbackListener>())
        }

        @Test
        fun shouldInjectPlayerMapObjectCallbackListenerAsSingleton() {
            val playerMapObjectCallbackListener = injector.getInstance<PlayerMapObjectCallbackListener>()

            assertThat(playerMapObjectCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<PlayerMapObjectCallbackListener>())
        }

        @Test
        fun shouldInjectPlayerCallbackListenerAsSingleton() {
            val playerCallbackListener = injector.getInstance<PlayerCallbackListener>()

            assertThat(playerCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<PlayerCallbackListener>())
        }

        @Test
        fun shouldInjectRaceCheckpointCallbackListenerAsSingleton() {
            val raceCheckpointCallbackListener = injector.getInstance<RaceCheckpointCallbackListener>()

            assertThat(raceCheckpointCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<RaceCheckpointCallbackListener>())
        }

        @Test
        fun shouldInjectTextDrawCallbackListenerAsSingleton() {
            val textDrawCallbackListener = injector.getInstance<TextDrawCallbackListener>()

            assertThat(textDrawCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<TextDrawCallbackListener>())
        }

        @Test
        fun shouldInjectPlayerTextDrawCallbackListenerAsSingleton() {
            val playerTextDrawCallbackListener = injector.getInstance<PlayerTextDrawCallbackListener>()

            assertThat(playerTextDrawCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<PlayerTextDrawCallbackListener>())
        }

        @Test
        fun shouldInjectVehicleCallbackListenerAsSingleton() {
            val vehicleCallbackListener = injector.getInstance<VehicleCallbackListener>()

            assertThat(vehicleCallbackListener)
                    .isNotNull
                    .isSameAs(injector.getInstance<VehicleCallbackListener>())
        }

        @Test
        fun shouldInjectCallbackListenerManagerAsSingleton() {
            val callbackListenerManager = injector.getInstance<CallbackListenerManager>()

            assertThat(callbackListenerManager)
                    .isNotNull
                    .isSameAs(injector.getInstance<CallbackListenerManager>())
        }

        @Test
        fun shouldInjectOnActorStreamInHandlerAsSingleton() {
            val onActorStreamInHandler = injector.getInstance<OnActorStreamInHandler>()

            assertThat(onActorStreamInHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onActorStreamInHandler)
        }

        @Test
        fun shouldInjectOnActorStreamOutHandlerAsSingleton() {
            val onActorStreamOutHandler = injector.getInstance<OnActorStreamOutHandler>()

            assertThat(onActorStreamOutHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onActorStreamOutHandler)
        }

        @Test
        fun shouldInjectOnDialogResponseHandlerAsSingleton() {
            val onDialogResponseHandler = injector.getInstance<OnDialogResponseHandler>()

            assertThat(onDialogResponseHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onDialogResponseHandler)
        }

        @Test
        fun shouldInjectOnEnterExitModShopHandlerAsSingleton() {
            val onEnterExitModShopHandler = injector.getInstance<OnEnterExitModShopHandler>()

            assertThat(onEnterExitModShopHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onEnterExitModShopHandler)
        }

        @Test
        fun shouldInjectOnGameModeExitHandlerAsSingleton() {
            val onGameModeExitHandler = injector.getInstance<OnGameModeExitHandler>()

            assertThat(onGameModeExitHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onGameModeExitHandler)
        }

        @Test
        fun shouldInjectOnGameModeInitHandlerAsSingleton() {
            val onGameModeInitHandler = injector.getInstance<OnGameModeInitHandler>()

            assertThat(onGameModeInitHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onGameModeInitHandler)
        }

        @Test
        fun shouldInjectOnIncomingConnectionHandlerAsSingleton() {
            val onIncomingConnectionHandler = injector.getInstance<OnIncomingConnectionHandler>()

            assertThat(onIncomingConnectionHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onIncomingConnectionHandler)
        }

        @Test
        fun shouldInjectOnObjectMovedHandlerAsSingleton() {
            val onObjectMovedHandler = injector.getInstance<OnObjectMovedHandler>()

            assertThat(onObjectMovedHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onObjectMovedHandler)
        }

        @Test
        fun shouldInjectOnPlayerClickMapHandlerAsSingleton() {
            val onPlayerClickMapHandler = injector.getInstance<OnPlayerClickMapHandler>()

            assertThat(onPlayerClickMapHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerClickMapHandler)
        }

        @Test
        fun shouldInjectOnPlayerClickPlayerHandlerAsSingleton() {
            val onPlayerClickPlayerHandler = injector.getInstance<OnPlayerClickPlayerHandler>()

            assertThat(onPlayerClickPlayerHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerClickPlayerHandler)
        }

        @Test
        fun shouldInjectOnPlayerClickPlayerTextDrawHandlerAsSingleton() {
            val onPlayerClickPlayerTextDrawHandler = injector.getInstance<OnPlayerClickPlayerTextDrawHandler>()

            assertThat(onPlayerClickPlayerTextDrawHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerClickPlayerTextDrawHandler)
        }

        @Test
        fun shouldInjectOnPlayerClickTextDrawHandlerAsSingleton() {
            val onPlayerClickTextDrawHandler = injector.getInstance<OnPlayerClickTextDrawHandler>()

            assertThat(onPlayerClickTextDrawHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerClickTextDrawHandler)
        }

        @Test
        fun shouldInjectOnPlayerCommandTextHandlerAsSingleton() {
            val onPlayerCommandTextHandler = injector.getInstance<OnPlayerCommandTextHandler>()

            assertThat(onPlayerCommandTextHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerCommandTextHandler)
        }

        @Test
        fun shouldInjectOnPlayerConnectHandlerAsSingleton() {
            val onPlayerConnectHandler = injector.getInstance<OnPlayerConnectHandler>()

            assertThat(onPlayerConnectHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerConnectHandler)
        }

        @Test
        fun shouldInjectOnPlayerDeathHandlerAsSingleton() {
            val onPlayerDeathHandler = injector.getInstance<OnPlayerDeathHandler>()

            assertThat(onPlayerDeathHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerDeathHandler)
        }

        @Test
        fun shouldInjectOnPlayerDisconnectHandlerAsSingleton() {
            val onPlayerDisconnectHandler = injector.getInstance<OnPlayerDisconnectHandler>()

            assertThat(onPlayerDisconnectHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerDisconnectHandler)
        }

        @Test
        fun shouldInjectOnPlayerEditAttachedObjectHandlerAsSingleton() {
            val onPlayerEditAttachedObjectHandler = injector.getInstance<OnPlayerEditAttachedObjectHandler>()

            assertThat(onPlayerEditAttachedObjectHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerEditAttachedObjectHandler)
        }

        @Test
        fun shouldInjectOnPlayerEditMapObjectHandlerAsSingleton() {
            val onPlayerEditMapObjectHandler = injector.getInstance<OnPlayerEditMapObjectHandler>()

            assertThat(onPlayerEditMapObjectHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerEditMapObjectHandler)
        }

        @Test
        fun shouldInjectOnPlayerEditPlayerMapObjectHandlerAsSingleton() {
            val onPlayerEditPlayerMapObjectHandler = injector.getInstance<OnPlayerEditPlayerMapObjectHandler>()

            assertThat(onPlayerEditPlayerMapObjectHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerEditPlayerMapObjectHandler)
        }

        @Test
        fun shouldInjectOnPlayerEnterCheckpointHandlerAsSingleton() {
            val onPlayerEnterCheckpointHandler = injector.getInstance<OnPlayerEnterCheckpointHandler>()

            assertThat(onPlayerEnterCheckpointHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerEnterCheckpointHandler)
        }

        @Test
        fun shouldInjectOnPlayerEnterRaceCheckpointHandlerAsSingleton() {
            val onPlayerEnterRaceCheckpointHandler = injector.getInstance<OnPlayerEnterRaceCheckpointHandler>()

            assertThat(onPlayerEnterRaceCheckpointHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerEnterRaceCheckpointHandler)
        }

        @Test
        fun shouldInjectOnPlayerEnterVehicleHandlerAsSingleton() {
            val onPlayerEnterVehicleHandler = injector.getInstance<OnPlayerEnterVehicleHandler>()

            assertThat(onPlayerEnterVehicleHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerEnterVehicleHandler)
        }

        @Test
        fun shouldInjectOnPlayerExitedMenuHandlerAsSingleton() {
            val onPlayerExitedMenuHandler = injector.getInstance<OnPlayerExitedMenuHandler>()

            assertThat(onPlayerExitedMenuHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerExitedMenuHandler)
        }

        @Test
        fun shouldInjectOnPlayerExitVehicleHandlerAsSingleton() {
            val onPlayerExitVehicleHandler = injector.getInstance<OnPlayerExitVehicleHandler>()

            assertThat(onPlayerExitVehicleHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerExitVehicleHandler)
        }

        @Test
        fun shouldInjectOnPlayerGiveDamageActorHandlerAsSingleton() {
            val onPlayerGiveDamageActorHandler = injector.getInstance<OnPlayerGiveDamageActorHandler>()

            assertThat(onPlayerGiveDamageActorHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerGiveDamageActorHandler)
        }

        @Test
        fun shouldInjectOnPlayerGiveDamageHandlerAsSingleton() {
            val onPlayerGiveDamageHandler = injector.getInstance<OnPlayerGiveDamageHandler>()

            assertThat(onPlayerGiveDamageHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerGiveDamageHandler)
        }

        @Test
        fun shouldInjectOnPlayerInteriorChangeHandlerAsSingleton() {
            val onPlayerInteriorChangeHandler = injector.getInstance<OnPlayerInteriorChangeHandler>()

            assertThat(onPlayerInteriorChangeHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerInteriorChangeHandler)
        }

        @Test
        fun shouldInjectOnPlayerKeyStateChangeHandlerAsSingleton() {
            val onPlayerKeyStateChangeHandler = injector.getInstance<OnPlayerKeyStateChangeHandler>()

            assertThat(onPlayerKeyStateChangeHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerKeyStateChangeHandler)
        }

        @Test
        fun shouldInjectOnPlayerLeaveCheckpointHandlerAsSingleton() {
            val onPlayerLeaveCheckpointHandler = injector.getInstance<OnPlayerLeaveCheckpointHandler>()

            assertThat(onPlayerLeaveCheckpointHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerLeaveCheckpointHandler)
        }

        @Test
        fun shouldInjectOnPlayerLeaveRaceCheckpointHandlerAsSingleton() {
            val onPlayerLeaveRaceCheckpointHandler = injector.getInstance<OnPlayerLeaveRaceCheckpointHandler>()

            assertThat(onPlayerLeaveRaceCheckpointHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerLeaveRaceCheckpointHandler)
        }

        @Test
        fun shouldInjectOnPlayerObjectMovedHandlerAsSingleton() {
            val onPlayerObjectMovedHandler = injector.getInstance<OnPlayerObjectMovedHandler>()

            assertThat(onPlayerObjectMovedHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerObjectMovedHandler)
        }

        @Test
        fun shouldInjectOnPlayerPickUpPickupHandlerAsSingleton() {
            val onPlayerPickUpPickupHandler = injector.getInstance<OnPlayerPickUpPickupHandler>()

            assertThat(onPlayerPickUpPickupHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerPickUpPickupHandler)
        }

        @Test
        fun shouldInjectOnPlayerRequestClassHandlerAsSingleton() {
            val onPlayerRequestClassHandler = injector.getInstance<OnPlayerRequestClassHandler>()

            assertThat(onPlayerRequestClassHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerRequestClassHandler)
        }

        @Test
        fun shouldInjectOnPlayerRequestSpawnHandlerAsSingleton() {
            val onPlayerRequestSpawnHandler = injector.getInstance<OnPlayerRequestSpawnHandler>()

            assertThat(onPlayerRequestSpawnHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerRequestSpawnHandler)
        }

        @Test
        fun shouldInjectOnPlayerSelectedMenuRowHandlerAsSingleton() {
            val onPlayerSelectedMenuRowHandler = injector.getInstance<OnPlayerSelectedMenuRowHandler>()

            assertThat(onPlayerSelectedMenuRowHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerSelectedMenuRowHandler)
        }

        @Test
        fun shouldInjectOnPlayerSelectMapObjectHandlerAsSingleton() {
            val onPlayerSelectMapObjectHandler = injector.getInstance<OnPlayerSelectMapObjectHandler>()

            assertThat(onPlayerSelectMapObjectHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerSelectMapObjectHandler)
        }

        @Test
        fun shouldInjectOnPlayerSelectPlayerMapObjectHandlerAsSingleton() {
            val onPlayerSelectPlayerMapObjectHandler = injector.getInstance<OnPlayerSelectPlayerMapObjectHandler>()

            assertThat(onPlayerSelectPlayerMapObjectHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerSelectPlayerMapObjectHandler)
        }

        @Test
        fun shouldInjectOnPlayerSpawnHandlerAsSingleton() {
            val onPlayerSpawnHandler = injector.getInstance<OnPlayerSpawnHandler>()

            assertThat(onPlayerSpawnHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerSpawnHandler)
        }

        @Test
        fun shouldInjectOnPlayerStateChangeHandlerAsSingleton() {
            val onPlayerStateChangeHandler = injector.getInstance<OnPlayerStateChangeHandler>()

            assertThat(onPlayerStateChangeHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerStateChangeHandler)
        }

        @Test
        fun shouldInjectOnPlayerStreamInHandlerAsSingleton() {
            val onPlayerStreamInHandler = injector.getInstance<OnPlayerStreamInHandler>()

            assertThat(onPlayerStreamInHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerStreamInHandler)
        }

        @Test
        fun shouldInjectOnPlayerStreamOutHandlerAsSingleton() {
            val onPlayerStreamOutHandler = injector.getInstance<OnPlayerStreamOutHandler>()

            assertThat(onPlayerStreamOutHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerStreamOutHandler)
        }

        @Test
        fun shouldInjectOnPlayerTakeDamageHandlerAsSingleton() {
            val onPlayerTakeDamageHandler = injector.getInstance<OnPlayerTakeDamageHandler>()

            assertThat(onPlayerTakeDamageHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerTakeDamageHandler)
        }

        @Test
        fun shouldInjectOnPlayerTextHandlerAsSingleton() {
            val onPlayerTextHandler = injector.getInstance<OnPlayerTextHandler>()

            assertThat(onPlayerTextHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerTextHandler)
        }

        @Test
        fun shouldInjectOnPlayerUpdateHandlerAsSingleton() {
            val onPlayerUpdateHandler = injector.getInstance<OnPlayerUpdateHandler>()

            assertThat(onPlayerUpdateHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerUpdateHandler)
        }

        @Test
        fun shouldInjectOnPlayerWeaponShotHandlerAsSingleton() {
            val onPlayerWeaponShotHandler = injector.getInstance<OnPlayerWeaponShotHandler>()

            assertThat(onPlayerWeaponShotHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onPlayerWeaponShotHandler)
        }

        @Test
        fun shouldInjectOnProcessTickHandlerAsSingleton() {
            val onProcessTickHandler = injector.getInstance<OnProcessTickHandler>()

            assertThat(onProcessTickHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onProcessTickHandler)
        }

        @Test
        fun shouldInjectOnRconCommandHandlerAsSingleton() {
            val onRconCommandHandler = injector.getInstance<OnRconCommandHandler>()

            assertThat(onRconCommandHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onRconCommandHandler)
        }

        @Test
        fun shouldInjectOnRconLoginAttemptHandlerAsSingleton() {
            val onRconLoginAttemptHandler = injector.getInstance<OnRconLoginAttemptHandler>()

            assertThat(onRconLoginAttemptHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onRconLoginAttemptHandler)
        }

        @Test
        fun shouldInjectOnTrailerUpdateHandlerAsSingleton() {
            val onTrailerUpdateHandler = injector.getInstance<OnTrailerUpdateHandler>()

            assertThat(onTrailerUpdateHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onTrailerUpdateHandler)
        }

        @Test
        fun shouldInjectOnUnoccupiedVehicleUpdateHandlerAsSingleton() {
            val onUnoccupiedVehicleUpdateHandler = injector.getInstance<OnUnoccupiedVehicleUpdateHandler>()

            assertThat(onUnoccupiedVehicleUpdateHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onUnoccupiedVehicleUpdateHandler)
        }

        @Test
        fun shouldInjectOnVehicleDamageStatusUpdateHandlerAsSingleton() {
            val onVehicleDamageStatusUpdateHandler = injector.getInstance<OnVehicleDamageStatusUpdateHandler>()

            assertThat(onVehicleDamageStatusUpdateHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehicleDamageStatusUpdateHandler)
        }

        @Test
        fun shouldInjectOnVehicleDeathHandlerAsSingleton() {
            val onVehicleDeathHandler = injector.getInstance<OnVehicleDeathHandler>()

            assertThat(onVehicleDeathHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehicleDeathHandler)
        }

        @Test
        fun shouldInjectOnVehicleModHandlerAsSingleton() {
            val onVehicleModHandler = injector.getInstance<OnVehicleModHandler>()

            assertThat(onVehicleModHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehicleModHandler)
        }

        @Test
        fun shouldInjectOnVehiclePaintjobHandlerAsSingleton() {
            val onVehiclePaintjobHandler = injector.getInstance<OnVehiclePaintjobHandler>()

            assertThat(onVehiclePaintjobHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehiclePaintjobHandler)
        }

        @Test
        fun shouldInjectOnVehicleResprayHandlerAsSingleton() {
            val onVehicleResprayHandler = injector.getInstance<OnVehicleResprayHandler>()

            assertThat(onVehicleResprayHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehicleResprayHandler)
        }

        @Test
        fun shouldInjectOnVehicleSirenStateChangeHandlerAsSingleton() {
            val onVehicleSirenStateChangeHandler = injector.getInstance<OnVehicleSirenStateChangeHandler>()

            assertThat(onVehicleSirenStateChangeHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehicleSirenStateChangeHandler)
        }

        @Test
        fun shouldInjectOnVehicleSpawnHandlerAsSingleton() {
            val onVehicleSpawnHandler = injector.getInstance<OnVehicleSpawnHandler>()

            assertThat(onVehicleSpawnHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehicleSpawnHandler)
        }

        @Test
        fun shouldInjectOnVehicleStreamInHandlerAsSingleton() {
            val onVehicleStreamInHandler = injector.getInstance<OnVehicleStreamInHandler>()

            assertThat(onVehicleStreamInHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehicleStreamInHandler)
        }

        @Test
        fun shouldInjectOnVehicleStreamOutHandlerAsSingleton() {
            val onVehicleStreamOutHandler = injector.getInstance<OnVehicleStreamOutHandler>()

            assertThat(onVehicleStreamOutHandler)
                    .isNotNull
            assertThat(testService.handlers)
                    .contains(onVehicleStreamOutHandler)
        }

    }

    private class TestService
    @Inject constructor(val handlers: Set<@JvmSuppressWildcards CallbackListenerRegistry<*>>)

}