package ch.leadrian.samp.kamp.core.api.timer

import java.util.concurrent.TimeUnit

interface TimerExecutor {

    fun addTimer(interval: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit): Timer

    fun addRepeatingTimer(interval: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit): Timer

    fun addRepeatingTimer(
            repetitions: Int,
            interval: Long,
            timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
            action: () -> Unit
    ): Timer

}