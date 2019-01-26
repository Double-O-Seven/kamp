package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

internal class MapObjectCoordinatesPropertyTest {

    private val mapObjectId: MapObjectId = MapObjectId.valueOf(50)
    private val mapObject: MapObject = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var mapObjectCoordinatesProperty: MapObjectCoordinatesProperty

    @BeforeEach
    fun setUp() {
        every { mapObject.id } returns mapObjectId
        mapObjectCoordinatesProperty = MapObjectCoordinatesProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetCoordinates() {
        every { nativeFunctionExecutor.getObjectPos(mapObjectId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }

        val coordinates = mapObjectCoordinatesProperty.getValue(mapObject, property)

        assertThat(coordinates)
                .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
    }

    @Test
    fun shouldSetCoordinates() {
        every { nativeFunctionExecutor.setObjectPos(any(), any(), any(), any()) } returns true

        mapObjectCoordinatesProperty.setValue(mapObject, property, vector3DOf(x = 1f, y = 2f, z = 3f))

        verify { nativeFunctionExecutor.setObjectPos(objectid = mapObjectId.value, x = 1f, y = 2f, z = 3f) }
    }
}