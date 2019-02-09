package ch.leadrian.samp.kamp.common.neon

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.service.MapObjectService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class DefaultVehicleNeonMapObjectProviderTest {

    @ParameterizedTest
    @EnumSource(NeonColor::class)
    fun shouldCreateMapObject(neonColor: NeonColor) {
        val expectedMapObject = mockk<MapObject>()
        val mapObjectService = mockk<MapObjectService> {
            every {
                createMapObject(neonColor.modelId, vector3DOf(0f, 0f, 0f), vector3DOf(0f, 0f, 0f))
            } returns expectedMapObject
        }
        val defaultVehicleNeonMapObjectProvider = DefaultVehicleNeonMapObjectProvider(mapObjectService)

        val mapObject = defaultVehicleNeonMapObjectProvider.createNeon(neonColor)

        assertThat(mapObject)
                .isEqualTo(expectedMapObject)
    }

}