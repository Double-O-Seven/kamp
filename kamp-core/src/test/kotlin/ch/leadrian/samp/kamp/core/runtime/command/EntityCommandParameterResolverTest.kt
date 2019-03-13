package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.core.api.entity.id.EntityId
import ch.leadrian.samp.kamp.core.api.entity.registry.EntityRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EntityCommandParameterResolverTest {

    private lateinit var entityCommandParameterResolver: EntityCommandParameterResolver<Entity<EntityId>, EntityId>
    private val entityRegistry = mockk<EntityRegistry<Entity<EntityId>, EntityId>>()

    @BeforeEach
    fun setUp() {
        entityCommandParameterResolver = TestEntityCommandParameterResolver(entityRegistry)
    }

    @Test
    fun givenValidEntityIdItShouldReturnEntity() {
        val expectedEntity = mockk<Entity<EntityId>>()
        every { entityRegistry[123] } returns expectedEntity

        val entity = entityCommandParameterResolver.resolve("123")

        assertThat(entity)
                .isEqualTo(expectedEntity)
    }

    @Test
    fun givenInvalidEntityIdItShouldReturnNull() {
        every { entityRegistry[123] } returns null

        val entity = entityCommandParameterResolver.resolve("123")

        assertThat(entity)
                .isNull()
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val entity = entityCommandParameterResolver.resolve("hahaha")

        assertThat(entity)
                .isNull()
    }

    private class TestEntityCommandParameterResolver(entityRegistry: EntityRegistry<Entity<EntityId>, EntityId>) :
            EntityCommandParameterResolver<Entity<EntityId>, EntityId>(entityRegistry) {

        override val parameterType: Class<Entity<EntityId>>
            get() = throw UnsupportedOperationException()

    }

}