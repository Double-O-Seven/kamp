package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.runtime.data.TimeImpl
import java.time.LocalDateTime
import java.time.LocalTime

interface Time {

    val hour: Int

    val minute: Int

    fun toTime(): Time

    fun toMutableTime(): MutableTime

}

fun timeOf(hour: Int, minute: Int): Time = TimeImpl(
        hour = hour,
        minute = minute
)

fun timeOf(localDateTime: LocalDateTime): Time = timeOf(hour = localDateTime.hour, minute = localDateTime.minute)

fun timeOf(localTime: LocalTime): Time = timeOf(hour = localTime.hour, minute = localTime.minute)
