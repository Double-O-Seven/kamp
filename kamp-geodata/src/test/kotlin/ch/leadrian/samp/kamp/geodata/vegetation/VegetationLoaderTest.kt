package ch.leadrian.samp.kamp.geodata.vegetation

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VegetationLoaderTest {

    private val vegetationLoader = VegetationLoader()

    @Test
    fun shouldLoadVegetation() {
        val vegetationProcessor = mockk<VegetationProcessor>(relaxed = true)

        vegetationLoader.load(vegetationProcessor)

        val vegetationObjects = mutableListOf<VegetationObject>()
        verify {
            vegetationProcessor.process(capture(vegetationObjects))
        }
        assertThat(vegetationObjects)
                .containsExactlyInAnyOrder(
                        VegetationObject(
                                modelId = 866,
                                coordinates = vector3DOf(2921.053955f, 2148.100585f, 10.876887f),
                                rotation = vector3DOf(0.000000f, 0.000000f, -54.340015f)
                        ),
                        VegetationObject(
                                modelId = 866,
                                coordinates = vector3DOf(2924.045410f, 2146.577636f, 11.523280f),
                                rotation = vector3DOf(0.000000f, 0.000000f, 147.968383f)
                        )
                )
    }

    @Test
    fun shouldProcessVegetationInline() {
        val vegetationProcessor = mockk<(VegetationObject) -> Unit>(relaxed = true)

        vegetationLoader.load(vegetationProcessor)

        val vegetationObjects = mutableListOf<VegetationObject>()
        verify {
            vegetationProcessor.invoke(capture(vegetationObjects))
        }
        assertThat(vegetationObjects)
                .containsExactlyInAnyOrder(
                        VegetationObject(
                                modelId = 866,
                                coordinates = vector3DOf(2921.053955f, 2148.100585f, 10.876887f),
                                rotation = vector3DOf(0.000000f, 0.000000f, -54.340015f)
                        ),
                        VegetationObject(
                                modelId = 866,
                                coordinates = vector3DOf(2924.045410f, 2146.577636f, 11.523280f),
                                rotation = vector3DOf(0.000000f, 0.000000f, 147.968383f)
                        )
                )
    }

}