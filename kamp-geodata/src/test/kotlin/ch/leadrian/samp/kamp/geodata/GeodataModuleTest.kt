package ch.leadrian.samp.kamp.geodata

import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.geodata.hmap.HeightMap
import ch.leadrian.samp.kamp.geodata.node.PathNodeService
import ch.leadrian.samp.kamp.geodata.vegetation.VegetationLoader
import com.google.inject.Guice
import com.google.inject.Injector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GeodataModuleTest {

    private val modules = arrayOf(GeodataModule())

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
        fun shouldInjectHeightMapAsSingleton() {
            val heightMap = injector.getInstance<HeightMap>()

            assertThat(heightMap)
                    .isNotNull
                    .isSameAs(injector.getInstance<HeightMap>())
        }

        @Test
        fun shouldInjectPathNodeServiceAsSingleton() {
            val pathNodeService = injector.getInstance<PathNodeService>()

            assertThat(pathNodeService)
                    .isNotNull
                    .isSameAs(injector.getInstance<PathNodeService>())
        }

        @Test
        fun shouldInjectVegetationLoader() {
            val vegetationLoader = injector.getInstance<VegetationLoader>()

            assertThat(vegetationLoader)
                    .isNotNull
        }
    }

}