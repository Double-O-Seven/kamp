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

internal class MapObjectRotationPropertyTest {

    private val mapObjectId: MapObjectId = MapObjectId.valueOf(50)
    private val mapObject: MapObject = mockk()
    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor = mockk()
    private val property: KProperty<Vector3D> = mockk()

    private lateinit var mapObjectRotationProperty: MapObjectRotationProperty

    @BeforeEach
    fun setUp() {
        every { mapObject.id } returns mapObjectId
        mapObjectRotationProperty = MapObjectRotationProperty(nativeFunctionExecutor)
    }

    @Test
    fun shouldGetRotation() {
        every { nativeFunctionExecutor.getObjectRot(mapObjectId.value, any(), any(), any()) } answers {
            secondArg<ReferenceFloat>().value = 1f
            thirdArg<ReferenceFloat>().value = 2f
            arg<ReferenceFloat>(3).value = 3f
            true
        }

        val rotation = mapObjectRotationProperty.getValue(mapObject, property)

        assertThat(rotation)
                .isEqualTo(vector3DOf(x = 1f, y = 2f, z = 3f))
    }

    @Test
    fun shouldSetRotation() {
        every { nativeFunctionExecutor.setObjectRot(any(), any(), any(), any()) } returns true

        mapObjectRotationProperty.setValue(mapObject, property, vector3DOf(x = 1f, y = 2f, z = 3f))

        verify { nativeFunctionExecutor.setObjectRot(objectid = mapObjectId.value, rotX = 1f, rotY = 2f, rotZ = 3f) }
    }
}