package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.MapObject
import ch.leadrian.samp.kamp.api.entity.id.MapObjectId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MapObjectRegistryTest {

    @ParameterizedTest
    @ValueSource(ints = [0, SAMPConstants.MAX_OBJECTS - 1])
    fun shouldRegisterAndGetMapObject(mapObjectId: Int) {
        val mapObject = mockk<MapObject> {
            every { id } returns MapObjectId.valueOf(mapObjectId)
        }
        val mapObjectRegistry = MapObjectRegistry()

        mapObjectRegistry.register(mapObject)

        val registeredMapObject = mapObjectRegistry.get(mapObjectId)
        Assertions.assertThat(registeredMapObject)
                .isSameAs(mapObject)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_OBJECTS, SAMPConstants.MAX_OBJECTS + 1])
    fun givenUnknownMapObjectIdGetMapObjectShouldReturn(mapObjectId: Int) {
        val mapObjectRegistry = MapObjectRegistry()

        val registeredMapObject = mapObjectRegistry.get(mapObjectId)
        Assertions.assertThat(registeredMapObject)
                .isNull()
    }

    @Test
    fun givenAnotherMapObjectWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val mapObjectId = 50
        val alreadyRegisteredMapObject = mockk<MapObject> {
            every { id } returns MapObjectId.valueOf(mapObjectId)
        }
        val newMapObject = mockk<MapObject> {
            every { id } returns MapObjectId.valueOf(mapObjectId)
        }
        val mapObjectRegistry = MapObjectRegistry()
        mapObjectRegistry.register(alreadyRegisteredMapObject)

        val caughtThrowable = Assertions.catchThrowable { mapObjectRegistry.register(newMapObject) }

        Assertions.assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredMapObject = mapObjectRegistry.get(mapObjectId)
        Assertions.assertThat(registeredMapObject)
                .isSameAs(alreadyRegisteredMapObject)
    }

    @Test
    fun shouldUnregisterRegisteredMapObject() {
        val mapObjectId = 50
        val mapObject = mockk<MapObject> {
            every { id } returns MapObjectId.valueOf(mapObjectId)
        }
        val mapObjectRegistry = MapObjectRegistry()
        mapObjectRegistry.register(mapObject)

        mapObjectRegistry.unregister(mapObject)

        val registeredMapObject = mapObjectRegistry.get(mapObjectId)
        Assertions.assertThat(registeredMapObject)
                .isNull()
    }


    @Test
    fun givenMapObjectIsNotRegisteredItShouldThrowAnException() {
        val mapObjectId = MapObjectId.valueOf(50)
        val mapObject = mockk<MapObject> {
            every { id } returns mapObjectId
        }
        val mapObjectRegistry = MapObjectRegistry()

        val caughtThrowable = Assertions.catchThrowable { mapObjectRegistry.unregister(mapObject) }

        Assertions.assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherMapObjectWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val mapObjectId = 50
        val alreadyRegisteredMapObject = mockk<MapObject> {
            every { id } returns MapObjectId.valueOf(mapObjectId)
        }
        val newMapObject = mockk<MapObject> {
            every { id } returns MapObjectId.valueOf(mapObjectId)
        }
        val mapObjectRegistry = MapObjectRegistry()
        mapObjectRegistry.register(alreadyRegisteredMapObject)

        val caughtThrowable = Assertions.catchThrowable { mapObjectRegistry.unregister(newMapObject) }

        Assertions.assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredMapObject = mapObjectRegistry.get(mapObjectId)
        Assertions.assertThat(registeredMapObject)
                .isSameAs(alreadyRegisteredMapObject)
    }

    @Test
    fun shouldReturnAllRegisteredMapObjects() {
        val mapObjectId1 = MapObjectId.valueOf(10)
        val mapObject1 = mockk<MapObject> {
            every { id } returns mapObjectId1
        }
        val mapObjectId2 = MapObjectId.valueOf(15)
        val mapObject2 = mockk<MapObject> {
            every { id } returns mapObjectId2
        }
        val mapObjectId3 = MapObjectId.valueOf(30)
        val mapObject3 = mockk<MapObject> {
            every { id } returns mapObjectId3
        }
        val mapObjectRegistry = MapObjectRegistry()
        mapObjectRegistry.register(mapObject1)
        mapObjectRegistry.register(mapObject2)
        mapObjectRegistry.register(mapObject3)

        val allMapObjects = mapObjectRegistry.getAll()

        Assertions.assertThat(allMapObjects)
                .containsExactly(mapObject1, mapObject2, mapObject3)
    }
}