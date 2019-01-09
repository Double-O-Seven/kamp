package ch.leadrian.samp.kamp.core.api.constants

import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DefaultPlayerColorsTest {

    @Test
    fun shouldReturnExpectedColor() {
        val color = DefaultPlayerColors[PlayerId.valueOf(4)]

        assertThat(color)
                .isEqualTo(colorOf(0x6495EDFF))
    }


    @Test
    fun shouldWrapAroundWithHighPlayerIds() {
        val color = DefaultPlayerColors[PlayerId.valueOf(102)]

        assertThat(color)
                .isEqualTo(colorOf(0x20B2AAFF))
    }

}