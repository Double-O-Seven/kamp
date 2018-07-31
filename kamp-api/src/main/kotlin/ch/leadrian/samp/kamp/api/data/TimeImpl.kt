package ch.leadrian.samp.kamp.api.data

internal data class TimeImpl(
        override val hour: Int,
        override val minute: Int
) : Time {

    override fun toTime(): Time = this

    override fun toMutableTime(): MutableTime = MutableTimeImpl(
            hour = hour,
            minute = minute
    )
}