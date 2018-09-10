package ch.leadrian.samp.kamp.core.api.data

interface MutableTime : Time {

    override var hour: Int

    override var minute: Int

}

fun mutableTimeOf(hour: Int, minute: Int): MutableTime = MutableTimeImpl(
        hour = hour,
        minute = minute
)