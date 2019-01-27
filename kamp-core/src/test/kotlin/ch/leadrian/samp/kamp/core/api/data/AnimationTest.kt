package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class AnimationTest {

    @Test
    fun shouldCreateAnimation() {
        val animation = animationOf(library = "lib", name = "test")

        assertAll(
                { assertThat(animation.library).isEqualTo("lib") },
                { assertThat(animation.animationName).isEqualTo("test") }
        )
    }

}