package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutableTime
import ch.leadrian.samp.kamp.core.api.data.Time

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