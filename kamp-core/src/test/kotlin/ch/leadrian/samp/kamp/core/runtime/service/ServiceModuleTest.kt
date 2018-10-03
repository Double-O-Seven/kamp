package ch.leadrian.samp.kamp.core.runtime.service

import ch.leadrian.samp.kamp.core.api.service.ActorService
import ch.leadrian.samp.kamp.core.api.service.CheckpointService
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.service.GangZoneService
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import ch.leadrian.samp.kamp.core.api.service.MenuService
import ch.leadrian.samp.kamp.core.api.service.PickupService
import ch.leadrian.samp.kamp.core.api.service.PlayerClassService
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.core.api.service.PlayerTextLabelService
import ch.leadrian.samp.kamp.core.api.service.RaceCheckpointService
import ch.leadrian.samp.kamp.core.api.service.ServerService
import ch.leadrian.samp.kamp.core.api.service.TextLabelService
import ch.leadrian.samp.kamp.core.api.service.VehicleService
import ch.leadrian.samp.kamp.core.api.service.WorldService
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackModule
import ch.leadrian.samp.kamp.core.runtime.text.TextModule
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ServiceModuleTest {

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(
                    ServiceModule(),
                    TextModule(),
                    CallbackModule(),
                    TestModule()
            )
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(
                    ServiceModule(),
                    TextModule(),
                    CallbackModule(),
                    TestModule()
            )
        }

        @Test
        fun shouldInjectActorService() {
            val actorService = injector.getInstance<ActorService>()

            assertThat(actorService)
                    .isNotNull
        }

        @Test
        fun shouldInjectCheckpointService() {
            val checkpointService = injector.getInstance<CheckpointService>()

            assertThat(checkpointService)
                    .isNotNull
        }

        @Test
        fun shouldInjectDialogService() {
            val dialogService = injector.getInstance<DialogService>()

            assertThat(dialogService)
                    .isNotNull
        }

        @Test
        fun shouldInjectGangZoneService() {
            val gangZoneService = injector.getInstance<GangZoneService>()

            assertThat(gangZoneService)
                    .isNotNull
        }

        @Test
        fun shouldInjectMapObjectService() {
            val mapObjectService = injector.getInstance<MapObjectService>()

            assertThat(mapObjectService)
                    .isNotNull
        }

        @Test
        fun shouldInjectMenuService() {
            val menuService = injector.getInstance<MenuService>()

            assertThat(menuService)
                    .isNotNull
        }

        @Test
        fun shouldInjectPickupService() {
            val pickupService = injector.getInstance<PickupService>()

            assertThat(pickupService)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerClassService() {
            val playerClassService = injector.getInstance<PlayerClassService>()

            assertThat(playerClassService)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerMapObjectService() {
            val playerMapObjectService = injector.getInstance<PlayerMapObjectService>()

            assertThat(playerMapObjectService)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerService() {
            val playerService = injector.getInstance<PlayerService>()

            assertThat(playerService)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerTextLabelService() {
            val playerTextLabelService = injector.getInstance<PlayerTextLabelService>()

            assertThat(playerTextLabelService)
                    .isNotNull
        }

        @Test
        fun shouldInjectRaceCheckpointService() {
            val raceCheckpointService = injector.getInstance<RaceCheckpointService>()

            assertThat(raceCheckpointService)
                    .isNotNull
        }

        @Test
        fun shouldInjectServerService() {
            val serverService = injector.getInstance<ServerService>()

            assertThat(serverService)
                    .isNotNull
        }

        @Test
        fun shouldInjectTextLabelService() {
            val textLabelService = injector.getInstance<TextLabelService>()

            assertThat(textLabelService)
                    .isNotNull
        }

        @Test
        fun shouldInjectVehicleServiceAsSingleton() {
            val vehicleService = injector.getInstance<VehicleService>()

            assertThat(vehicleService)
                    .isNotNull
                    .isSameAs(injector.getInstance<VehicleService>())
        }

        @Test
        fun shouldInjectWorldService() {
            val worldService = injector.getInstance<WorldService>()

            assertThat(worldService)
                    .isNotNull
        }
    }

    private class TestModule : AbstractModule() {

        override fun configure() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every { getMaxPlayers() } returns 50
            }
            bind(SAMPNativeFunctionExecutor::class.java).toInstance(nativeFunctionExecutor)
        }

    }
}