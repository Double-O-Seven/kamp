package ch.leadrian.samp.kamp.core.api.timer

import java.util.concurrent.TimeUnit

interface TimerExecutor {

    fun addTimer(interval: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit)

    fun addRepeatingTimer(interval: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit)

    fun addRepeatingTimer(interval: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, repetions: Int, action: () -> Unit)

}