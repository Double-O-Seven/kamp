package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.runtime.entity.PlayerMapObjectImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerMapObjectRegistryTest {

    @ParameterizedTest
    @ValueSource(ints = [0, SAMPConstants.MAX_OBJECTS - 1])
    fun shouldRegisterAndGetPlayerMapObject(playerMapObjectId: Int) {
        val playerMapObject = mockk<PlayerMapObjectImpl> {
            every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
        }
        val playerMapObjectRegistry = PlayerMapObjectRegistry()

        playerMapObjectRegistry.register(playerMapObject)

        val registeredPlayerMapObject = playerMapObjectRegistry[playerMapObjectId]
        Assertions.assertThat(registeredPlayerMapObject)
                .isSameAs(playerMapObject)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_OBJECTS, SAMPConstants.MAX_OBJECTS + 1])
    fun givenUnknownPlayerMapObjectIdGetPlayerMapObjectShouldReturn(playerMapObjectId: Int) {
        val playerMapObjectRegistry = PlayerMapObjectRegistry()

        val registeredPlayerMapObject = playerMapObjectRegistry[playerMapObjectId]
        Assertions.assertThat(registeredPlayerMapObject)
                .isNull()
    }

    @Test
    fun givenAnotherPlayerMapObjectWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val playerMapObjectId = 50
        val alreadyRegisteredPlayerMapObject = mockk<PlayerMapObjectImpl> {
            every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
        }
        val newPlayerMapObject = mockk<PlayerMapObjectImpl> {
            every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
        }
        val playerMapObjectRegistry = PlayerMapObjectRegistry()
        playerMapObjectRegistry.register(alreadyRegisteredPlayerMapObject)

        val caughtThrowable = Assertions.catchThrowable { playerMapObjectRegistry.register(newPlayerMapObject) }

        Assertions.assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredPlayerMapObject = playerMapObjectRegistry[playerMapObjectId]
        Assertions.assertThat(registeredPlayerMapObject)
                .isSameAs(alreadyRegisteredPlayerMapObject)
    }

    @Test
    fun shouldUnregisterRegisteredPlayerMapObject() {
        val playerMapObjectId = 50
        val playerMapObject = mockk<PlayerMapObjectImpl> {
            every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
        }
        val playerMapObjectRegistry = PlayerMapObjectRegistry()
        playerMapObjectRegistry.register(playerMapObject)

        playerMapObjectRegistry.unregister(playerMapObject)

        val registeredPlayerMapObject = playerMapObjectRegistry[playerMapObjectId]
        Assertions.assertThat(registeredPlayerMapObject)
                .isNull()
    }


    @Test
    fun givenPlayerMapObjectIsNotRegisteredItShouldThrowAnException() {
        val playerMapObjectId = PlayerMapObjectId.valueOf(50)
        val playerMapObject = mockk<PlayerMapObjectImpl> {
            every { id } returns playerMapObjectId
        }
        val playerMapObjectRegistry = PlayerMapObjectRegistry()

        val caughtThrowable = Assertions.catchThrowable { playerMapObjectRegistry.unregister(playerMapObject) }

        Assertions.assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherPlayerMapObjectWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val playerMapObjectId = 50
        val alreadyRegisteredPlayerMapObject = mockk<PlayerMapObjectImpl> {
            every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
        }
        val newPlayerMapObject = mockk<PlayerMapObjectImpl> {
            every { id } returns PlayerMapObjectId.valueOf(playerMapObjectId)
        }
        val playerMapObjectRegistry = PlayerMapObjectRegistry()
        playerMapObjectRegistry.register(alreadyRegisteredPlayerMapObject)

        val caughtThrowable = Assertions.catchThrowable { playerMapObjectRegistry.unregister(newPlayerMapObject) }

        Assertions.assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredPlayerMapObject = playerMapObjectRegistry[playerMapObjectId]
        Assertions.assertThat(registeredPlayerMapObject)
                .isSameAs(alreadyRegisteredPlayerMapObject)
    }

    @Test
    fun shouldReturnAllRegisteredPlayerMapObjects() {
        val playerMapObjectId1 = PlayerMapObjectId.valueOf(10)
        val playerMapObject1 = mockk<PlayerMapObjectImpl> {
            every { id } returns playerMapObjectId1
        }
        val playerMapObjectId2 = PlayerMapObjectId.valueOf(15)
        val playerMapObject2 = mockk<PlayerMapObjectImpl> {
            every { id } returns playerMapObjectId2
        }
        val playerMapObjectId3 = PlayerMapObjectId.valueOf(30)
        val playerMapObject3 = mockk<PlayerMapObjectImpl> {
            every { id } returns playerMapObjectId3
        }
        val playerMapObjectRegistry = PlayerMapObjectRegistry()
        playerMapObjectRegistry.register(playerMapObject1)
        playerMapObjectRegistry.register(playerMapObject2)
        playerMapObjectRegistry.register(playerMapObject3)

        val allPlayerMapObjects = playerMapObjectRegistry.getAll()

        Assertions.assertThat(allPlayerMapObjects)
                .containsExactly(playerMapObject1, playerMapObject2, playerMapObject3)
    }
}