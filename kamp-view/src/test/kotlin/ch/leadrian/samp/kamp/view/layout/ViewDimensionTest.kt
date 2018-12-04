package ch.leadrian.samp.kamp.view.layout

import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ViewDimensionTest {

    @Test
    fun shouldReturnPixelValue() {
        val pixels = 50.pixels()

        val value = pixels.getValue(1337f)

        assertThat(value)
                .isEqualTo(50f)
    }

    @Test
    fun shouldComputePixelValue() {
        val pixels = pixels { 50f }

        val value = pixels.getValue(1337f)

        assertThat(value)
                .isEqualTo(50f)
    }

    @Test
    fun shouldReturnPercentValue() {
        val percent = 75.percent()

        val value = percent.getValue(2000f)

        assertThat(value)
                .isEqualTo(1500f)
    }

    @Test
    fun shouldComputePercentValue() {
        val percent = percent { 75f }

        val value = percent.getValue(2000f)

        assertThat(value)
                .isEqualTo(1500f)
    }

}