package ch.leadrian.samp.kamp.view.screenresolution

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ScreenResolutionTest {

    @Test
    fun shouldCreateScreenResolutionUsingInfixFunction() {
        val screenResolution = 640 x 480

        assertThat(screenResolution)
                .isEqualTo(ScreenResolution(width = 640, height = 480))
    }

}