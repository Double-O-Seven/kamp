package ch.leadrian.samp.kamp.core.api.timer

import ch.leadrian.samp.kamp.core.runtime.timer.TimerExecutorImpl
import com.google.inject.ImplementedBy
import java.util.concurrent.TimeUnit

@ImplementedBy(TimerExecutorImpl::class)
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