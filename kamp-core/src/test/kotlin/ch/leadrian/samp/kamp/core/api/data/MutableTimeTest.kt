package ch.leadrian.samp.kamp.core.api.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.LocalTime

internal class MutableTimeTest {

    @Test
    fun toTimeShouldReturnTime() {
        val hour = 13
        val minute = 37
        val mutableTime = mutableTimeOf(
                hour = hour,
                minute = minute
        )

        val time = mutableTime.toTime()

        assertThat(time)
                .isNotInstanceOf(MutableTime::class.java)
                .satisfies {
                    assertThat(it.hour)
                            .isEqualTo(hour)
                    assertThat(it.minute)
                            .isEqualTo(minute)
                }
    }

    @Test
    fun toMutableTimeShouldReturnSameInstance() {
        val hour = 13
        val minute = 37
        val expectedMutableTime = mutableTimeOf(
                hour = hour,
                minute = minute
        )

        val mutableTime = expectedMutableTime.toMutableTime()

        assertThat(mutableTime)
                .isSameAs(expectedMutableTime)
    }

    @Test
    fun shouldConvertLocalDateTimeToMutableTime() {
        val time = mutableTimeOf(LocalDateTime.of(1992, 2, 4, 15, 45))

        assertThat(time)
                .isEqualTo(mutableTimeOf(hour = 15, minute = 45))
    }

    @Test
    fun shouldConvertLocalTimeToMutableTime() {
        val time = mutableTimeOf(LocalTime.of(15, 45))

        assertThat(time)
                .isEqualTo(mutableTimeOf(hour = 15, minute = 45))
    }
}