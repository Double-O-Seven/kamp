package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TimeTest {

    @Test
    fun toTimeShouldReturnSameInstance() {
        val hour = 13
        val minute = 37
        val expectedTime = timeOf(
                hour = hour,
                minute = minute
        )

        val time = expectedTime.toTime()

        assertThat(time)
                .isSameAs(expectedTime)
    }

    @Test
    fun toMutableTimeShouldReturnMutableTime() {
        val hour = 13
        val minute = 37
        val time = timeOf(
                hour = hour,
                minute = minute
        )

        val mutableTime = time.toMutableTime()

        assertThat(mutableTime)
                .satisfies {
                    assertThat(it.hour)
                            .isEqualTo(hour)
                    assertThat(it.minute)
                            .isEqualTo(minute)
                }
    }
}