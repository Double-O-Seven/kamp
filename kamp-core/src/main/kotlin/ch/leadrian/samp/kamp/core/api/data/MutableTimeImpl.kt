package ch.leadrian.samp.kamp.core.api.data

internal data class MutableTimeImpl(
        override var hour: Int,
        override var minute: Int
) : MutableTime {

    override fun toTime(): Time = TimeImpl(
            hour = hour,
            minute = minute
    )

    override fun toMutableTime(): MutableTime = this
}