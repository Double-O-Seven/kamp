package ch.leadrian.samp.kamp.core.runtime.async

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnProcessTickListener
import ch.leadrian.samp.kamp.core.api.exception.UncaughtExceptionNotifier
import ch.leadrian.samp.kamp.core.api.service.ServerService
import ch.leadrian.samp.kamp.core.api.util.ExecutorServiceFactory
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import java.util.concurrent.CompletableFuture
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
        private val serverService: ServerService,
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

    @com.google.inject.Inject(optional = true)
    internal var uncaughtExceptionNotifier: UncaughtExceptionNotifier? = null

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
            try {
                val task = mainThreadTasks.poll() ?: break
                task.invoke()
            } catch (e: Exception) {
                log.error("Exception while processing main thread tasks", e)
                notifyAboutException(e)
            }
        } while (true)
    }

    override fun execute(onSuccess: (() -> Unit)?, onFailure: ((Exception) -> Unit)?, action: AsyncExecutor.() -> Unit) {
        executorService.execute {
            try {
                action.invoke(this)
                onSuccess?.let { executeOnMainThread(it) }
            } catch (e: Exception) {
                log.error("Exception in asynchronous execution", e)
                handleFailure(e, onFailure)
            }
        }
    }

    override fun <T> executeWithResult(onSuccess: (T) -> Unit, onFailure: ((Exception) -> Unit)?, action: AsyncExecutor.() -> T) {
        executorService.execute {
            try {
                val result = action.invoke(this)
                executeOnMainThread { onSuccess(result) }
            } catch (e: Exception) {
                log.error("Exception in asynchronous execution with result", e)
                handleFailure(e, onFailure)
            }
        }
    }

    private fun handleFailure(e: Exception, onFailure: ((Exception) -> Unit)?) {
        when {
            onFailure != null -> executeOnMainThread { onFailure(e) }
            uncaughtExceptionNotifier != null -> executeOnMainThread { notifyAboutException(e) }
        }
    }

    private fun notifyAboutException(exception: Exception) {
        try {
            uncaughtExceptionNotifier?.notify(exception)
        } catch (e: Exception) {
            log.error("Exception while notifying {} about exception", uncaughtExceptionNotifier, e)
        }
    }

    override fun executeOnMainThread(action: () -> Unit) {
        if (serverService.isOnMainThread()) {
            action()
        } else {
            mainThreadTasks.add(action)
        }
    }

    override fun <T> computeOnMainThread(action: () -> T): CompletableFuture<T> {
        val result = CompletableFuture<T>()
        executeOnMainThread {
            try {
                result.complete(action())
            } catch (e: Exception) {
                result.completeExceptionally(e)
            }
        }
        return result
    }

}