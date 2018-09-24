package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class EntityRegistryModuleTest {

    private val entityRegistryModule = EntityRegistryModule()

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(TestModule(), entityRegistryModule)
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(TestModule(), entityRegistryModule)
        }

        @Test
        fun shouldInjectActorRegistryAsSingleton() {
            val actorRegistry = injector.getInstance<ActorRegistry>()

            assertThat(actorRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<ActorRegistry>())
        }

        @Test
        fun shouldInjectGangZoneRegistryAsSingleton() {
            val gangZoneRegistry = injector.getInstance<GangZoneRegistry>()

            assertThat(gangZoneRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<GangZoneRegistry>())
        }

        @Test
        fun shouldInjectMapObjectRegistryAsSingleton() {
            val mapObjectRegistry = injector.getInstance<MapObjectRegistry>()

            assertThat(mapObjectRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<MapObjectRegistry>())
        }

        @Test
        fun shouldInjectMenuRegistryAsSingleton() {
            val menuRegistry = injector.getInstance<MenuRegistry>()

            assertThat(menuRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<MenuRegistry>())
        }

        @Test
        fun shouldInjectPickupRegistryAsSingleton() {
            val pickupRegistry = injector.getInstance<PickupRegistry>()

            assertThat(pickupRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<PickupRegistry>())
        }

        @Test
        fun shouldInjectPlayerClassRegistryAsSingleton() {
            val playerClassRegistry = injector.getInstance<PlayerClassRegistry>()

            assertThat(playerClassRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<PlayerClassRegistry>())
        }

        @Test
        fun shouldInjectPlayerRegistryAsSingleton() {
            val playerRegistry = injector.getInstance<PlayerRegistry>()

            assertThat(playerRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<PlayerRegistry>())
        }

        @Test
        fun shouldInjectTextDrawRegistryAsSingleton() {
            val textDrawRegistry = injector.getInstance<TextDrawRegistry>()

            assertThat(textDrawRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<TextDrawRegistry>())
        }

        @Test
        fun shouldInjectTextLabelRegistryAsSingleton() {
            val textLabelRegistry = injector.getInstance<TextLabelRegistry>()

            assertThat(textLabelRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<TextLabelRegistry>())
        }

        @Test
        fun shouldInjectVehicleRegistryAsSingleton() {
            val vehicleRegistry = injector.getInstance<VehicleRegistry>()

            assertThat(vehicleRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<VehicleRegistry>())
        }
    }

    private class TestModule : AbstractModule() {

        override fun configure() {
            bind(SAMPNativeFunctionExecutor::class.java).toInstance(mockk(relaxed = true))
        }

    }

}