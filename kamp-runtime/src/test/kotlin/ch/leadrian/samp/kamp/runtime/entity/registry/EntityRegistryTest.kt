package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.entity.Entity
import ch.leadrian.samp.kamp.api.entity.id.EntityId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class EntityRegistryTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 25, 49])
    fun shouldRegisterAndGetTestEntity(testId: Int) {
        val testEntity = mockk<TestEntity> {
            every { id } returns TestId(testId)
        }
        val testEntityRegistry = TestEntityRegistry(50)

        testEntityRegistry.register(testEntity)

        assertThat(testEntityRegistry[TestId(testId)])
                .isSameAs(testEntity)
        assertThat(testEntityRegistry[testId])
                .isSameAs(testEntity)
        assertThat(testEntityRegistry.getAll())
                .containsExactlyInAnyOrder(testEntity)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 50, 51])
    fun givenUnknownTestIdGetTestEntityShouldReturn(testId: Int) {
        val testEntityRegistry = TestEntityRegistry(50)

        assertThat(testEntityRegistry[TestId(testId)])
                .isNull()
        assertThat(testEntityRegistry[testId])
                .isNull()
        assertThat(testEntityRegistry.getAll())
                .isEmpty()
    }

    @Test
    fun givenAnotherTestEntityWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val testId = TestId(50)
        val alreadyRegisteredTestEntity = mockk<TestEntity> {
            every { id } returns testId
        }
        val newTestEntity = mockk<TestEntity> {
            every { id } returns testId
        }
        val testEntityRegistry = TestEntityRegistry(75)
        testEntityRegistry.register(alreadyRegisteredTestEntity)

        val caughtThrowable = catchThrowable { testEntityRegistry.register(newTestEntity) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredTestEntity = testEntityRegistry[testId]
        assertThat(registeredTestEntity)
                .isSameAs(alreadyRegisteredTestEntity)
    }

    @Test
    fun shouldUnregisterRegisteredTestEntity() {
        val testId = TestId(50)
        val testEntity = mockk<TestEntity> {
            every { id } returns testId
        }
        val testEntityRegistry = TestEntityRegistry(75)
        testEntityRegistry.register(testEntity)

        testEntityRegistry.unregister(testEntity)

        assertThat(testEntityRegistry[testId])
                .isNull()
        assertThat(testEntityRegistry[testId.value])
                .isNull()
        assertThat(testEntityRegistry.getAll())
                .isEmpty()
    }

    @Test
    fun givenTestEntityIsNotRegisteredItShouldThrowAnException() {
        val testId = TestId(50)
        val testEntity = mockk<TestEntity> {
            every { id } returns testId
        }
        val testEntityRegistry = TestEntityRegistry(75)

        val caughtThrowable = catchThrowable { testEntityRegistry.unregister(testEntity) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherTestEntityWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val testId = TestId(50)
        val alreadyRegisteredTestEntity = mockk<TestEntity> {
            every { id } returns testId
        }
        val newTestEntity = mockk<TestEntity> {
            every { id } returns testId
        }
        val testEntityRegistry = TestEntityRegistry(75)
        testEntityRegistry.register(alreadyRegisteredTestEntity)

        val caughtThrowable = catchThrowable { testEntityRegistry.unregister(newTestEntity) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredTestEntity = testEntityRegistry[testId]
        assertThat(registeredTestEntity)
                .isSameAs(alreadyRegisteredTestEntity)
    }

    @Test
    fun shouldReturnAllRegisteredTestEntities() {
        val testId1 = TestId(10)
        val testEntity1 = mockk<TestEntity> {
            every { id } returns testId1
        }
        val testId2 = TestId(15)
        val testEntity2 = mockk<TestEntity> {
            every { id } returns testId2
        }
        val testId3 = TestId(30)
        val testEntity3 = mockk<TestEntity> {
            every { id } returns testId3
        }
        val testEntityRegistry = TestEntityRegistry(50)
        testEntityRegistry.register(testEntity1)
        testEntityRegistry.register(testEntity2)
        testEntityRegistry.register(testEntity3)

        val allTestEntities = testEntityRegistry.getAll()

        assertThat(allTestEntities)
                .containsExactly(testEntity1, testEntity2, testEntity3)
    }

    private class TestEntityRegistry(numberOfEntities: Int) : EntityRegistry<TestEntity, TestId>(arrayOfNulls(numberOfEntities))

    private class TestEntity(override val id: TestId) : Entity<TestId>

    private class TestId(override val value: Int) : EntityId

}