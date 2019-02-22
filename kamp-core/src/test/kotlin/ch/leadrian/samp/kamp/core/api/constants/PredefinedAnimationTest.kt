package ch.leadrian.samp.kamp.core.api.constants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PredefinedAnimationTest {

    @Test
    fun shouldReturnLibrary() {
        val library = PredefinedAnimation.BAR_BARman_idle.library

        assertThat(library)
                .isEqualTo("BAR")
    }

    @Test
    fun shouldReturnAnimationName() {
        val animationName = PredefinedAnimation.BAR_BARman_idle.animationName

        assertThat(animationName)
                .isEqualTo("BARman_idle")
    }

}