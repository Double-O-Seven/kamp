package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.MutableTime
import ch.leadrian.samp.kamp.core.api.data.Time

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