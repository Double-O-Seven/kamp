package ch.leadrian.samp.kamp.core.api.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AbstractDestroyableTest {

    @Test
    fun givenDestroyableIsNotDestroyedFinalizeShouldCallDestroy() {
        val destroyable = TestDestroyable()

        destroyable.finalize()

        assertThat(destroyable.numberOfDestroyCalls)
                .isEqualTo(1)
    }

    @Test
    fun givenDestroyableIsDestroyedFinalizeShouldNotCallDestroy() {
        val destroyable = TestDestroyable()
        destroyable.isDestroyed = true

        destroyable.finalize()

        assertThat(destroyable.numberOfDestroyCalls)
                .isEqualTo(0)
    }

    private class TestDestroyable : AbstractDestroyable() {

        var numberOfDestroyCalls = 0

        override var isDestroyed: Boolean = false

        override fun destroy() {
            isDestroyed = true
            numberOfDestroyCalls++
        }

        public override fun finalize() = super.finalize()

    }

}