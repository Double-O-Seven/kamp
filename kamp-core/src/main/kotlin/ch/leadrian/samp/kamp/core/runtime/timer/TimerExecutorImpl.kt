package ch.leadrian.samp.kamp.core.runtime.timer

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.timer.Timer
import ch.leadrian.samp.kamp.core.api.timer.TimerExecutor
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.core.runtime.async.ExecutorServiceFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TimerExecutorImpl
@Inject
constructor(
        private val asyncExecutor: AsyncExecutor,
        private val executorServiceFactory: ExecutorServiceFactory
) : TimerExecutor {

    private companion object {

        val log = loggerFor<TimerExecutorImpl>()

    }

    private var started: Boolean = false

    private val scheduledExecutorService: ScheduledExecutorService by lazy {
        executorServiceFactory.createSingleThreadScheduledExecutor().also {
            started = true
        }
    }

    @PreDestroy
    fun shutdown() {
        if (started) {
            scheduledExecutorService.apply {
                log.info("Shutting down...")
                shutdown()
                log.info("Awaiting termination...")
                val terminated = awaitTermination(3, TimeUnit.MINUTES)
                if (terminated) {
                    log.info("Terminated")
                } else {
                    log.warn("Failed to terminate after 3 minutes")
                }
            }
        }
    }

    override fun addTimer(interval: Long, timeUnit: TimeUnit, action: () -> Unit): Timer {
        val timer = SimpleTimer(action)
        timer.scheduledFuture = scheduledExecutorService.schedule(timer, interval, timeUnit)
        return timer
    }

    override fun addRepeatingTimer(interval: Long, timeUnit: TimeUnit, action: () -> Unit): Timer {
        val timer = SimpleTimer(action)
        timer.scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(timer, interval, interval, timeUnit)
        return timer
    }

    override fun addRepeatingTimer(repetitions: Int, interval: Long, timeUnit: TimeUnit, action: () -> Unit): Timer {
        val timer = RepeatingTimer(repetitions, action)
        timer.scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(timer, interval, interval, timeUnit)
        return timer
    }

    private abstract class AbstractTimer : Timer, Runnable {

        lateinit var scheduledFuture: ScheduledFuture<*>

        final override fun stop() {
            while (!this::scheduledFuture.isInitialized) {
                Thread.yield()
            }
            if (!scheduledFuture.isCancelled) {
                scheduledFuture.cancel(false)
            }
        }

    }

    private inner class SimpleTimer(private val action: () -> Unit) : AbstractTimer() {

        override fun run() {
            asyncExecutor.executeOnMainThread(action)
        }

    }

    private inner class RepeatingTimer(private val maxRepetitions: Int, private val action: () -> Unit) : AbstractTimer() {

        private var currentRepetition = 0

        override fun run() {
            asyncExecutor.executeOnMainThread(action)
            currentRepetition++
            if (currentRepetition >= maxRepetitions) {
                stop()
            }
        }

    }

}