package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.MapObjectFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MapObjectRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MapObjectServiceTest {

    private lateinit var mapObjectService: MapObjectService

    private val mapObjectFactory = mockk<MapObjectFactory>()
    private val mapObjectRegistry = mockk<MapObjectRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        mapObjectService = MapObjectService(mapObjectFactory, mapObjectRegistry, nativeFunctionExecutor)
    }

    @Test
    fun shouldCreateMapObject() {
        val expectedMapObject = mockk<MapObject>()
        every {
            mapObjectFactory.create(
                    model = 1337,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                    drawDistance = 7f
            )
        } returns expectedMapObject

        val mapObject = mapObjectService.createMapObject(
                modelId = 1337,
                coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                drawDistance = 7f
        )

        assertThat(mapObject)
                .isEqualTo(expectedMapObject)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun isValidShouldReturnExpectedResult(expectedResult: Boolean) {
        val mapObjectId = MapObjectId.valueOf(1337)
        every { nativeFunctionExecutor.isValidObject(mapObjectId.value) } returns expectedResult

        val result = mapObjectService.isValid(mapObjectId)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Nested
    inner class GetMapObjectTests {

        @Test
        fun givenMapObjectIdIsValidItShouldReturnMapObject() {
            val mapObjectId = MapObjectId.valueOf(1337)
            val expectedMapObject = mockk<MapObject>()
            every { mapObjectRegistry[mapObjectId] } returns expectedMapObject

            val mapObject = mapObjectService.getMapObject(mapObjectId)

            assertThat(mapObject)
                    .isEqualTo(expectedMapObject)
        }

        @Test
        fun givenInvalidMapObjectIdItShouldThrowException() {
            val mapObjectId = MapObjectId.valueOf(1337)
            every { mapObjectRegistry[mapObjectId] } returns null

            val caughtThrowable = catchThrowable { mapObjectService.getMapObject(mapObjectId) }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No map object with ID 1337")
        }

    }

    @Test
    fun shouldReturnAllMapObjects() {
        val mapObject1 = mockk<MapObject>()
        val mapObject2 = mockk<MapObject>()
        every { mapObjectRegistry.getAll() } returns listOf(mapObject1, mapObject2)

        val mapObjects = mapObjectService.getAllMapObjects()

        assertThat(mapObjects)
                .containsExactly(mapObject1, mapObject2)
    }

    @Test
    fun shouldEnableCameraCollisions() {
        every { nativeFunctionExecutor.setObjectsDefaultCameraCol(any()) } returns true

        mapObjectService.enableCameraCollisions()

        verify { nativeFunctionExecutor.setObjectsDefaultCameraCol(false) }
    }

    @Test
    fun shouldDisableCameraCollisions() {
        every { nativeFunctionExecutor.setObjectsDefaultCameraCol(any()) } returns true

        mapObjectService.disableCameraCollisions()

        verify { nativeFunctionExecutor.setObjectsDefaultCameraCol(true) }
    }

}