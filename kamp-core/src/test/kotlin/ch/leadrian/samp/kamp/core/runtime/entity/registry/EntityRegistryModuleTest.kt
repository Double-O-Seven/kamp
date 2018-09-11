package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
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

internal class EntityRegistryModuleTest {

    private val entityRegistryModule = EntityRegistryModule()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.getMaxPlayers() } returns 50
    }

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(MockModule(nativeFunctionExecutor), entityRegistryModule)
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(MockModule(nativeFunctionExecutor), entityRegistryModule)
        }

        @Test
        fun shouldInjectActorRegistry() {
            val actorRegistry = injector.getInstance<ActorRegistry>()

            assertThat(actorRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectGangZoneRegistry() {
            val gangZoneRegistry = injector.getInstance<GangZoneRegistry>()

            assertThat(gangZoneRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectMapObjectRegistry() {
            val mapObjectRegistry = injector.getInstance<MapObjectRegistry>()

            assertThat(mapObjectRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectMenuRegistry() {
            val menuRegistry = injector.getInstance<MenuRegistry>()

            assertThat(menuRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectPickupRegistry() {
            val pickupRegistry = injector.getInstance<PickupRegistry>()

            assertThat(pickupRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerClassRegistry() {
            val playerClassRegistry = injector.getInstance<PlayerClassRegistry>()

            assertThat(playerClassRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerRegistry() {
            val playerRegistry = injector.getInstance<PlayerRegistry>()

            assertThat(playerRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectTextDrawRegistry() {
            val textDrawRegistry = injector.getInstance<TextDrawRegistry>()

            assertThat(textDrawRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectTextLabelRegistry() {
            val textLabelRegistry = injector.getInstance<TextLabelRegistry>()

            assertThat(textLabelRegistry)
                    .isNotNull
        }

        @Test
        fun shouldInjectVehicleRegistry() {
            val vehicleRegistry = injector.getInstance<VehicleRegistry>()

            assertThat(vehicleRegistry)
                    .isNotNull
        }
    }

    private class MockModule(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) : AbstractModule() {

        override fun configure() {
            bind(SAMPNativeFunctionExecutor::class.java).toInstance(nativeFunctionExecutor)
        }

    }

}