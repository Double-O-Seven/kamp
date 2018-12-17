package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class DestroyableTest {

    @Nested
    inner class NonLambdaTests {

        @Test
        fun givenDestroyableIsDestroyedItShouldThrowAnException() {
            val destroyable = mockk<Destroyable> {
                every { isDestroyed } returns true
            }

            val caughtThrowable = catchThrowable { destroyable.requireNotDestroyed() }

            assertThat(caughtThrowable)
                    .isInstanceOf(AlreadyDestroyedException::class.java)
        }

        @Test
        fun givenDestroyableIsDestroyedNotItShouldNotThrowAnException() {
            val destroyable = mockk<Destroyable> {
                every { isDestroyed } returns false
            }

            val returnedDestroyable = destroyable.requireNotDestroyed()

            assertThat(returnedDestroyable)
                    .isSameAs(destroyable)
        }
    }

    @Nested
    inner class LambdaTests {

        @Test
        fun givenDestroyableIsDestroyedItShouldThrowAnException() {
            val block = mockk<Destroyable.() -> Unit>(relaxed = true)
            val destroyable = mockk<Destroyable> {
                every { isDestroyed } returns true
            }

            val caughtThrowable = catchThrowable { destroyable.requireNotDestroyed(block) }

            assertThat(caughtThrowable)
                    .isInstanceOf(AlreadyDestroyedException::class.java)
            verify { block wasNot Called }
        }

        @Test
        fun givenDestroyableIsNotDestroyedItShouldNotThrowAnException() {
            val destroyable = mockk<Destroyable> {
                every { isDestroyed } returns false
            }
            val block = mockk<Destroyable.() -> Int> {
                every { this@mockk.invoke(destroyable) } returns 1337
            }

            val result = destroyable.requireNotDestroyed(block)

            assertThat(result)
                    .isEqualTo(1337)
        }
    }

    @Nested
    inner class IfNotDestroyedTests {

        @Test
        fun givenItIsNotDestroyedItShouldReturnExpectedValue() {
            val destroyable = mockk<Destroyable> {
                every { isDestroyed } returns false
            }

            val result = destroyable.ifNotDestroyed { 1337 }

            assertThat(result)
                    .isEqualTo(1337)
        }

        @Test
        fun givenItIsDestroyedItShouldReturnNull() {
            val destroyable = mockk<Destroyable> {
                every { isDestroyed } returns true
            }

            val result = destroyable.ifNotDestroyed { 1337 }

            assertThat(result)
                    .isNull()
        }

    }

    @Nested
    inner class OnDestroyTests {

        @Test
        fun shouldAddOnDestroyListener() {
            val destroyable = mockk<Destroyable> {
                every { addOnDestroyListener(any()) } just Runs
            }

            val listener = destroyable.onDestroy {}

            verify { destroyable.addOnDestroyListener(listener) }
        }

        @Test
        fun listenerShouldCallAction() {
            val action = mockk<Destroyable.() -> Unit>(relaxed = true)
            val destroyable = mockk<Destroyable> {
                every { addOnDestroyListener(any()) } just Runs
            }
            val listener = destroyable.onDestroy(action)

            listener.onDestroy(destroyable)

            verify(exactly = 1) { action.invoke(destroyable) }
        }
    }

}