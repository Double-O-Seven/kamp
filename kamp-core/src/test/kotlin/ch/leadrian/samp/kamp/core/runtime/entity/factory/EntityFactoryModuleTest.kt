package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.TestModule
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackModule
import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistryModule
import ch.leadrian.samp.kamp.core.runtime.text.TextModule
import com.google.inject.Guice
import com.google.inject.Injector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class EntityFactoryModuleTest {

    private val modules = arrayOf(EntityFactoryModule(), TextModule(), EntityRegistryModule(), CallbackModule(), TestModule())

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(*modules)
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(*modules)
        }

        @Test
        fun shouldInjectActorFactory() {
            val actorFactory = injector.getInstance<ActorFactory>()

            assertThat(actorFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectGangZoneFactory() {
            val gangZoneFactory = injector.getInstance<GangZoneFactory>()

            assertThat(gangZoneFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectMapObjectFactory() {
            val mapObjectFactory = injector.getInstance<MapObjectFactory>()

            assertThat(mapObjectFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerMapObjectFactory() {
            val playerMapObjectFactory = injector.getInstance<PlayerMapObjectFactory>()

            assertThat(playerMapObjectFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerMapIconFactory() {
            val playerMapIconFactory = injector.getInstance<PlayerMapIconFactory>()

            assertThat(playerMapIconFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectMenuFactory() {
            val menuFactory = injector.getInstance<MenuFactory>()

            assertThat(menuFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectPickupFactory() {
            val pickupFactory = injector.getInstance<PickupFactory>()

            assertThat(pickupFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerClassFactory() {
            val playerClassFactory = injector.getInstance<PlayerClassFactory>()

            assertThat(playerClassFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerFactory() {
            val playerFactory = injector.getInstance<PlayerFactory>()

            assertThat(playerFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerTextDrawFactory() {
            val playerTextDrawFactory = injector.getInstance<PlayerTextDrawFactory>()

            assertThat(playerTextDrawFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectPlayerTextLabelFactory() {
            val playerTextLabelFactory = injector.getInstance<PlayerTextLabelFactory>()

            assertThat(playerTextLabelFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectTextDrawFactory() {
            val textDrawFactory = injector.getInstance<TextDrawFactory>()

            assertThat(textDrawFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectTextLabelFactory() {
            val textLabelFactory = injector.getInstance<TextLabelFactory>()

            assertThat(textLabelFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectVehicleFactory() {
            val vehicleFactory = injector.getInstance<VehicleFactory>()

            assertThat(vehicleFactory)
                    .isNotNull
        }
    }

}