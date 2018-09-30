package ch.leadrian.samp.kamp.core.runtime.async

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnProcessTickListener
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AsyncExecutorImpl
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        executorServiceFactory: ExecutorServiceFactory
) : AsyncExecutor, OnProcessTickListener {

    private companion object {

        val log = loggerFor<AsyncExecutorImpl>()

    }

    private var started: Boolean = false

    private val executorService: ExecutorService by lazy {
        executorServiceFactory.createSingleThreadExecutor().also {
            started = true
        }
    }

    private val mainThreadTasks = ConcurrentLinkedQueue<() -> Unit>()

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    @PreDestroy
    fun shutdown() {
        if (started) {
            executorService.apply {
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

    override fun onProcessTick() {
        do {
            val task = mainThreadTasks.poll() ?: break
            task.invoke()
        } while (true)
    }

    override fun execute(onSuccess: (() -> Unit)?, onFailure: ((Exception) -> Unit)?, action: AsyncExecutor.() -> Unit) {
        executorService.execute {
            try {
                action.invoke(this)
                onSuccess?.let { executeOnMainThread(it) }
            } catch (e: Exception) {
                onFailure?.let { executeOnMainThread { it(e) } }
                log.error("Exception in asynchronous execution", e)
            }
        }
    }

    override fun <T> executeWithResult(onSuccess: (T) -> Unit, onFailure: ((Exception) -> Unit)?, action: AsyncExecutor.() -> T) {
        executorService.execute {
            try {
                val result = action.invoke(this)
                executeOnMainThread { onSuccess(result) }
            } catch (e: Exception) {
                onFailure?.let { executeOnMainThread { it(e) } }
                log.error("Exception in asynchronous execution with result", e)
            }
        }
    }

    override fun executeOnMainThread(action: () -> Unit) {
        mainThreadTasks.add(action)
    }
}