package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.runtime.data.MutableTimeImpl
import java.time.LocalDateTime
import java.time.LocalTime

interface MutableTime : Time {

    override var hour: Int

    override var minute: Int

}

fun mutableTimeOf(hour: Int, minute: Int): MutableTime = MutableTimeImpl(
        hour = hour,
        minute = minute
)

fun mutableTimeOf(localDateTime: LocalDateTime): Time = mutableTimeOf(
        hour = localDateTime.hour,
        minute = localDateTime.minute
)

fun mutableTimeOf(localTime: LocalTime): Time = mutableTimeOf(hour = localTime.hour, minute = localTime.minute)