package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.LocalTime

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

    @Test
    fun shouldConvertLocalDateTimeToTime() {
        val time = timeOf(LocalDateTime.of(1992, 2, 4, 15, 45))

        assertThat(time)
                .isEqualTo(timeOf(hour = 15, minute = 45))
    }

    @Test
    fun shouldConvertLocalTimeToTime() {
        val time = timeOf(LocalTime.of(15, 45))

        assertThat(time)
                .isEqualTo(timeOf(hour = 15, minute = 45))
    }
}