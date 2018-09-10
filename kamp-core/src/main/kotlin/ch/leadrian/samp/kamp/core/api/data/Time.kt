package ch.leadrian.samp.kamp.core.api.data

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
