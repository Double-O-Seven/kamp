package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MapObjectFactoryTest {

    private lateinit var mapObjectFactory: MapObjectFactory

    private val mapObjectRegistry = mockk<MapObjectRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.createObject(any(), any(), any(), any(), any(), any(), any(), any()) } returns 0
        every { mapObjectRegistry.register(any()) } just Runs
        every { mapObjectRegistry.unregister(any()) } just Runs
        mapObjectFactory = MapObjectFactory(mapObjectRegistry, nativeFunctionExecutor)
    }

    @Test
    fun shouldCreateMapObject() {
        mapObjectFactory.create(
                model = 1337,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                drawDistance = 7f
        )

        verify {
            nativeFunctionExecutor.createObject(
                    modelid = 1337,
                    x = 1f,
                    y = 2f,
                    z = 3f,
                    rX = 4f,
                    rY = 5f,
                    rZ = 6f,
                    DrawDistance = 7f
            )
        }
    }

    @Test
    fun shouldRegisterMapObject() {
        val mapObject = mapObjectFactory.create(
                model = 1337,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                drawDistance = 7f
        )

        verify { mapObjectRegistry.register(mapObject) }
    }

    @Test
    fun shouldUnregisterMapObjectOnDestroy() {
        every { nativeFunctionExecutor.destroyObject(any()) } returns true
        val mapObject = mapObjectFactory.create(
                model = 1337,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                drawDistance = 7f
        )
        val onDestroy = mockk<MapObject.() -> Unit>(relaxed = true)
        mapObject.onDestroy(onDestroy)

        mapObject.destroy()

        verify { onDestroy.invoke(mapObject) }
    }

}