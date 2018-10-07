package ch.leadrian.samp.kamp.core.runtime.timer

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
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

    override fun addTimer(interval: Long, timeUnit: TimeUnit, action: () -> Unit) {
        scheduledExecutorService.schedule(Task(action), interval, timeUnit)
    }

    override fun addRepeatingTimer(interval: Long, timeUnit: TimeUnit, action: () -> Unit) {
        scheduledExecutorService.scheduleAtFixedRate(Task(action), interval, interval, timeUnit)
    }

    override fun addRepeatingTimer(interval: Long, timeUnit: TimeUnit, repetions: Int, action: () -> Unit) {
        val task = CancellableTask(repetions, action)
        val future = scheduledExecutorService.scheduleAtFixedRate(task, interval, interval, timeUnit)
        task.future = future
    }

    private inner class CancellableTask(
            private val repetitions: Int,
            private val action: () -> Unit
    ) : Runnable {

        lateinit var future: ScheduledFuture<*>

        private var currentRepetition = 0

        override fun run() {
            asyncExecutor.executeOnMainThread(action)
            currentRepetition++
            if (currentRepetition >= repetitions) {
                while (!this::future.isInitialized) {
                    Thread.yield()
                }
                future.cancel(false)
            }
        }

    }

    private inner class Task(private val action: () -> Unit) : Runnable {

        override fun run() {
            asyncExecutor.executeOnMainThread(action)
        }

    }

}