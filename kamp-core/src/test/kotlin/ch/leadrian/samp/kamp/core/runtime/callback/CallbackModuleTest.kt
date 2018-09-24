package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.util.getInstance
import com.google.inject.Guice
import com.google.inject.Injector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CallbackModuleTest {

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(CallbackModule())
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(CallbackModule())
        }

        @Test
        fun shouldInjectOnActorStreamInHandler() {
            val onActorStreamInHandler = injector.getInstance<OnActorStreamInHandler>()

            assertThat(onActorStreamInHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnActorStreamOutHandler() {
            val onActorStreamOutHandler = injector.getInstance<OnActorStreamOutHandler>()

            assertThat(onActorStreamOutHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnEnterExitModShopHandler() {
            val onEnterExitModShopHandler = injector.getInstance<OnEnterExitModShopHandler>()

            assertThat(onEnterExitModShopHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnGameModeExitHandler() {
            val onGameModeExitHandler = injector.getInstance<OnGameModeExitHandler>()

            assertThat(onGameModeExitHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnGameModeInitHandler() {
            val onGameModeInitHandler = injector.getInstance<OnGameModeInitHandler>()

            assertThat(onGameModeInitHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnIncomingConnectionHandler() {
            val onIncomingConnectionHandler = injector.getInstance<OnIncomingConnectionHandler>()

            assertThat(onIncomingConnectionHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnObjectMovedHandler() {
            val onObjectMovedHandler = injector.getInstance<OnObjectMovedHandler>()

            assertThat(onObjectMovedHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerClickMapHandler() {
            val onPlayerClickMapHandler = injector.getInstance<OnPlayerClickMapHandler>()

            assertThat(onPlayerClickMapHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerClickPlayerHandler() {
            val onPlayerClickPlayerHandler = injector.getInstance<OnPlayerClickPlayerHandler>()

            assertThat(onPlayerClickPlayerHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerClickPlayerTextDrawHandler() {
            val onPlayerClickPlayerTextDrawHandler = injector.getInstance<OnPlayerClickPlayerTextDrawHandler>()

            assertThat(onPlayerClickPlayerTextDrawHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerClickTextDrawHandler() {
            val onPlayerClickTextDrawHandler = injector.getInstance<OnPlayerClickTextDrawHandler>()

            assertThat(onPlayerClickTextDrawHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerCommandTextHandler() {
            val onPlayerCommandTextHandler = injector.getInstance<OnPlayerCommandTextHandler>()

            assertThat(onPlayerCommandTextHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerConnectHandler() {
            val onPlayerConnectHandler = injector.getInstance<OnPlayerConnectHandler>()

            assertThat(onPlayerConnectHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerDeathHandler() {
            val onPlayerDeathHandler = injector.getInstance<OnPlayerDeathHandler>()

            assertThat(onPlayerDeathHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerDisconnectHandler() {
            val onPlayerDisconnectHandler = injector.getInstance<OnPlayerDisconnectHandler>()

            assertThat(onPlayerDisconnectHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerEditAttachedObjectHandler() {
            val onPlayerEditAttachedObjectHandler = injector.getInstance<OnPlayerEditAttachedObjectHandler>()

            assertThat(onPlayerEditAttachedObjectHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerEditMapObjectHandler() {
            val onPlayerEditMapObjectHandler = injector.getInstance<OnPlayerEditMapObjectHandler>()

            assertThat(onPlayerEditMapObjectHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerEditPlayerMapObjectHandler() {
            val onPlayerEditPlayerMapObjectHandler = injector.getInstance<OnPlayerEditPlayerMapObjectHandler>()

            assertThat(onPlayerEditPlayerMapObjectHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerEnterCheckpointHandler() {
            val onPlayerEnterCheckpointHandler = injector.getInstance<OnPlayerEnterCheckpointHandler>()

            assertThat(onPlayerEnterCheckpointHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerEnterRaceCheckpointHandler() {
            val onPlayerEnterRaceCheckpointHandler = injector.getInstance<OnPlayerEnterRaceCheckpointHandler>()

            assertThat(onPlayerEnterRaceCheckpointHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerEnterVehicleHandler() {
            val onPlayerEnterVehicleHandler = injector.getInstance<OnPlayerEnterVehicleHandler>()

            assertThat(onPlayerEnterVehicleHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerExitedMenuHandler() {
            val onPlayerExitedMenuHandler = injector.getInstance<OnPlayerExitedMenuHandler>()

            assertThat(onPlayerExitedMenuHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerExitVehicleHandler() {
            val onPlayerExitVehicleHandler = injector.getInstance<OnPlayerExitVehicleHandler>()

            assertThat(onPlayerExitVehicleHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerGiveDamageActorHandler() {
            val onPlayerGiveDamageActorHandler = injector.getInstance<OnPlayerGiveDamageActorHandler>()

            assertThat(onPlayerGiveDamageActorHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerGiveDamageHandler() {
            val onPlayerGiveDamageHandler = injector.getInstance<OnPlayerGiveDamageHandler>()

            assertThat(onPlayerGiveDamageHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerInteriorChangeHandler() {
            val onPlayerInteriorChangeHandler = injector.getInstance<OnPlayerInteriorChangeHandler>()

            assertThat(onPlayerInteriorChangeHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerKeyStateChangeHandler() {
            val onPlayerKeyStateChangeHandler = injector.getInstance<OnPlayerKeyStateChangeHandler>()

            assertThat(onPlayerKeyStateChangeHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerLeaveCheckpointHandler() {
            val onPlayerLeaveCheckpointHandler = injector.getInstance<OnPlayerLeaveCheckpointHandler>()

            assertThat(onPlayerLeaveCheckpointHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerLeaveRaceCheckpointHandler() {
            val onPlayerLeaveRaceCheckpointHandler = injector.getInstance<OnPlayerLeaveRaceCheckpointHandler>()

            assertThat(onPlayerLeaveRaceCheckpointHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerObjectMovedHandler() {
            val onPlayerObjectMovedHandler = injector.getInstance<OnPlayerObjectMovedHandler>()

            assertThat(onPlayerObjectMovedHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerPickUpPickupHandler() {
            val onPlayerPickUpPickupHandler = injector.getInstance<OnPlayerPickUpPickupHandler>()

            assertThat(onPlayerPickUpPickupHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerRequestClassHandler() {
            val onPlayerRequestClassHandler = injector.getInstance<OnPlayerRequestClassHandler>()

            assertThat(onPlayerRequestClassHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerRequestSpawnHandler() {
            val onPlayerRequestSpawnHandler = injector.getInstance<OnPlayerRequestSpawnHandler>()

            assertThat(onPlayerRequestSpawnHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerSelectedMenuRowHandler() {
            val onPlayerSelectedMenuRowHandler = injector.getInstance<OnPlayerSelectedMenuRowHandler>()

            assertThat(onPlayerSelectedMenuRowHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerSelectMapObjectHandler() {
            val onPlayerSelectMapObjectHandler = injector.getInstance<OnPlayerSelectMapObjectHandler>()

            assertThat(onPlayerSelectMapObjectHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerSelectPlayerMapObjectHandler() {
            val onPlayerSelectPlayerMapObjectHandler = injector.getInstance<OnPlayerSelectPlayerMapObjectHandler>()

            assertThat(onPlayerSelectPlayerMapObjectHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerSpawnHandler() {
            val onPlayerSpawnHandler = injector.getInstance<OnPlayerSpawnHandler>()

            assertThat(onPlayerSpawnHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerStateChangeHandler() {
            val onPlayerStateChangeHandler = injector.getInstance<OnPlayerStateChangeHandler>()

            assertThat(onPlayerStateChangeHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerStreamInHandler() {
            val onPlayerStreamInHandler = injector.getInstance<OnPlayerStreamInHandler>()

            assertThat(onPlayerStreamInHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerStreamOutHandler() {
            val onPlayerStreamOutHandler = injector.getInstance<OnPlayerStreamOutHandler>()

            assertThat(onPlayerStreamOutHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerTakeDamageHandler() {
            val onPlayerTakeDamageHandler = injector.getInstance<OnPlayerTakeDamageHandler>()

            assertThat(onPlayerTakeDamageHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerTextHandler() {
            val onPlayerTextHandler = injector.getInstance<OnPlayerTextHandler>()

            assertThat(onPlayerTextHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerUpdateHandler() {
            val onPlayerUpdateHandler = injector.getInstance<OnPlayerUpdateHandler>()

            assertThat(onPlayerUpdateHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnPlayerWeaponShotHandler() {
            val onPlayerWeaponShotHandler = injector.getInstance<OnPlayerWeaponShotHandler>()

            assertThat(onPlayerWeaponShotHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnProcessTickHandler() {
            val onProcessTickHandler = injector.getInstance<OnProcessTickHandler>()

            assertThat(onProcessTickHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnRconCommandHandler() {
            val onRconCommandHandler = injector.getInstance<OnRconCommandHandler>()

            assertThat(onRconCommandHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnRconLoginAttemptHandler() {
            val onRconLoginAttemptHandler = injector.getInstance<OnRconLoginAttemptHandler>()

            assertThat(onRconLoginAttemptHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnTrailerUpdateHandler() {
            val onTrailerUpdateHandler = injector.getInstance<OnTrailerUpdateHandler>()

            assertThat(onTrailerUpdateHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnUnoccupiedVehicleUpdateHandler() {
            val onUnoccupiedVehicleUpdateHandler = injector.getInstance<OnUnoccupiedVehicleUpdateHandler>()

            assertThat(onUnoccupiedVehicleUpdateHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehicleDamageStatusUpdateHandler() {
            val onVehicleDamageStatusUpdateHandler = injector.getInstance<OnVehicleDamageStatusUpdateHandler>()

            assertThat(onVehicleDamageStatusUpdateHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehicleDeathHandler() {
            val onVehicleDeathHandler = injector.getInstance<OnVehicleDeathHandler>()

            assertThat(onVehicleDeathHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehicleModHandler() {
            val onVehicleModHandler = injector.getInstance<OnVehicleModHandler>()

            assertThat(onVehicleModHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehiclePaintjobHandler() {
            val onVehiclePaintjobHandler = injector.getInstance<OnVehiclePaintjobHandler>()

            assertThat(onVehiclePaintjobHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehicleResprayHandler() {
            val onVehicleResprayHandler = injector.getInstance<OnVehicleResprayHandler>()

            assertThat(onVehicleResprayHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehicleSirenStateChangeHandler() {
            val onVehicleSirenStateChangeHandler = injector.getInstance<OnVehicleSirenStateChangeHandler>()

            assertThat(onVehicleSirenStateChangeHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehicleSpawnHandler() {
            val onVehicleSpawnHandler = injector.getInstance<OnVehicleSpawnHandler>()

            assertThat(onVehicleSpawnHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehicleStreamInHandler() {
            val onVehicleStreamInHandler = injector.getInstance<OnVehicleStreamInHandler>()

            assertThat(onVehicleStreamInHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectOnVehicleStreamOutHandler() {
            val onVehicleStreamOutHandler = injector.getInstance<OnVehicleStreamOutHandler>()

            assertThat(onVehicleStreamOutHandler)
                    .isNotNull
        }


    }

}