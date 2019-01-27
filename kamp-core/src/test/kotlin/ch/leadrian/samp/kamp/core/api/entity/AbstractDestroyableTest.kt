package ch.leadrian.samp.kamp.core.api.entity

import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AbstractDestroyableTest {

    @Test
    fun givenDestroyableIsNotDestroyedFinalizeShouldCallDestroy() {
        val destroyable = TestDestroyable()

        destroyable.finalize()

        assertThat(destroyable.numberOfDestroyCalls)
                .isOne()
    }

    @Test
    fun isDestroyedShouldInitiallyBeFalse() {
        val destroyable = TestDestroyable()

        val isDestroyed = destroyable.isDestroyed

        assertThat(isDestroyed)
                .isFalse()
    }

    @Test
    fun isDestroyedShouldBeTrueAfterDestroyCall() {
        val destroyable = TestDestroyable()

        destroyable.destroy()

        assertThat(destroyable.isDestroyed)
                .isTrue()
    }

    @Test
    fun shouldCallOnDestroyOnlyOnce() {
        val destroyable = TestDestroyable()

        destroyable.destroy()
        destroyable.destroy()

        assertThat(destroyable.numberOfDestroyCalls)
                .isOne()
    }

    @Test
    fun givenDestroyableIsDestroyedFinalizeShouldNotCallDestroy() {
        val destroyable = TestDestroyable()
        destroyable.destroy()

        destroyable.finalize()

        assertThat(destroyable.numberOfDestroyCalls)
                .isOne()
    }

    @Test
    fun shouldCallOnDestroyListener() {
        val destroyable = TestDestroyable()
        val listener = mockk<OnDestroyListener>(relaxed = true)
        destroyable.addOnDestroyListener(listener)

        destroyable.destroy()

        verify { listener.onDestroy(destroyable) }
    }

    @Test
    fun shouldCallOnDestroyListenerOnlyOnce() {
        val destroyable = TestDestroyable()
        val listener = mockk<OnDestroyListener>(relaxed = true)
        destroyable.addOnDestroyListener(listener)

        destroyable.destroy()
        destroyable.destroy()

        verify(exactly = 1) { listener.onDestroy(destroyable) }
    }

    @Test
    fun shouldNotCallRemovedOnDestroyListener() {
        val destroyable = TestDestroyable()
        val listener = mockk<OnDestroyListener>(relaxed = true)
        destroyable.addOnDestroyListener(listener)
        destroyable.removeOnDestroyListener(listener)

        destroyable.destroy()

        verify(exactly = 0) { listener.onDestroy(destroyable) }
    }

    private class TestDestroyable : AbstractDestroyable() {

        var numberOfDestroyCalls = 0

        override fun onDestroy() {
            numberOfDestroyCalls++
        }

        public override fun finalize() = super.finalize()

    }

}