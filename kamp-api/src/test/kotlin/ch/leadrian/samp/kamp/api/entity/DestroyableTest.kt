package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.exception.AlreadyDestroyedException
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class DestroyableTest {

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