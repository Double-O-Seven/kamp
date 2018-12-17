package ch.leadrian.samp.kamp.core.api.entity.extension

import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.exception.EntityExtensionAlreadyInstalledException
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityExtensionException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class EntityExtensionContainerTest {

    private val testEntity = TestEntity()

    private lateinit var entityExtensionContainer: EntityExtensionContainer<TestEntity>

    @BeforeEach
    fun setUp() {
        entityExtensionContainer = EntityExtensionContainer(testEntity)
    }

    @Test
    fun shouldInstallExtension() {
        val extension = mockk<FooExtension>()
        val entityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, FooExtension>> {
            every { create(testEntity) } returns extension
            every { extensionClass } returns FooExtension::class
        }

        entityExtensionContainer.install(entityExtensionFactory)

        assertThat(entityExtensionContainer[FooExtension::class])
                .isEqualTo(extension)
    }

    @Test
    fun shouldInstallMultipleExtensions() {
        val fooExtension = mockk<FooExtension>()
        val fooEntityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, FooExtension>> {
            every { create(testEntity) } returns fooExtension
            every { extensionClass } returns FooExtension::class
        }
        val barExtension = mockk<BarExtension>()
        val barEntityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, BarExtension>> {
            every { create(testEntity) } returns barExtension
            every { extensionClass } returns BarExtension::class
        }

        entityExtensionContainer.install(fooEntityExtensionFactory)
        entityExtensionContainer.install(barEntityExtensionFactory)

        assertThat(entityExtensionContainer.getAll())
                .containsExactlyInAnyOrder(fooExtension, barExtension)
    }

    @Test
    fun shouldInlineGetExtension() {
        val extension = mockk<FooExtension>()
        val entityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, FooExtension>> {
            every { create(testEntity) } returns extension
            every { extensionClass } returns FooExtension::class
        }

        entityExtensionContainer.install(entityExtensionFactory)

        assertThat(entityExtensionContainer.get<FooExtension>())
                .isEqualTo(extension)
    }

    @Test
    fun givenExtensionIsAlreadyInstalledItShouldThrowException() {
        val extension = mockk<FooExtension>()
        val entityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, FooExtension>> {
            every { create(testEntity) } returns extension
            every { extensionClass } returns FooExtension::class
        }
        entityExtensionContainer.install(entityExtensionFactory)

        val caughtThrowable = catchThrowable { entityExtensionContainer.install(entityExtensionFactory) }

        assertThat(caughtThrowable)
                .isInstanceOf(EntityExtensionAlreadyInstalledException::class.java)
    }

    @Test
    fun givenNoSuchExtensionIsInstalledGetShouldThrowAnException() {
        val extension = mockk<FooExtension>()
        val entityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, FooExtension>> {
            every { create(testEntity) } returns extension
            every { extensionClass } returns FooExtension::class
        }
        entityExtensionContainer.install(entityExtensionFactory)

        val caughtThrowable = catchThrowable { entityExtensionContainer[BarExtension::class] }

        assertThat(caughtThrowable)
                .isInstanceOf(NoSuchEntityExtensionException::class.java)
    }

    @Test
    fun givenNoSuchExtensionIsInstalledInlineGetShouldThrowAnException() {
        val extension = mockk<FooExtension>()
        val entityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, FooExtension>> {
            every { create(testEntity) } returns extension
            every { extensionClass } returns FooExtension::class
        }
        entityExtensionContainer.install(entityExtensionFactory)

        val caughtThrowable = catchThrowable { entityExtensionContainer.get<BarExtension>() }

        assertThat(caughtThrowable)
                .isInstanceOf(NoSuchEntityExtensionException::class.java)
    }

    @Nested
    inner class DestroyTests {

        @Test
        fun shouldDestroyExtensions() {
            val fooExtension = mockk<FooExtension>(relaxed = true)
            val fooEntityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, FooExtension>> {
                every { create(testEntity) } returns fooExtension
                every { extensionClass } returns FooExtension::class
            }
            val barExtension = mockk<BarExtension>(relaxed = true)
            val barEntityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, BarExtension>> {
                every { create(testEntity) } returns barExtension
                every { extensionClass } returns BarExtension::class
            }
            val quxExtension = mockk<QuxExtension>(relaxed = true)
            val quxEntityExtensionFactory = mockk<EntityExtensionFactory<TestEntity, QuxExtension>> {
                every { create(testEntity) } returns quxExtension
                every { extensionClass } returns QuxExtension::class
            }
            entityExtensionContainer.install(barEntityExtensionFactory)
            entityExtensionContainer.install(fooEntityExtensionFactory)
            entityExtensionContainer.install(quxEntityExtensionFactory)

            entityExtensionContainer.destroy()

            verify {
                barExtension.destroy()
                quxExtension.destroy()
            }
        }

        @Test
        fun shouldNotBeDestroyedInitially() {
            assertThat(entityExtensionContainer.isDestroyed)
                    .isFalse()
        }

        @Test
        fun shouldBeDestroyed() {
            entityExtensionContainer.destroy()

            assertThat(entityExtensionContainer.isDestroyed)
                    .isTrue()
        }
    }

    private class FooExtension

    private class BarExtension : AbstractDestroyable() {

        override fun onDestroy() {}

    }

    private class QuxExtension : AbstractDestroyable() {

        override fun onDestroy() {}

    }

    private class TestEntity

}