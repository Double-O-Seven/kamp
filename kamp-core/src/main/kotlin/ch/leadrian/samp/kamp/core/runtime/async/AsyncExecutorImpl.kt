package ch.leadrian.samp.kamp.core.runtime.async

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnProcessTickListener
import ch.leadrian.samp.kamp.core.api.exception.SafeCaller
import ch.leadrian.samp.kamp.core.api.exception.UncaughtExceptionNotifier
import ch.leadrian.samp.kamp.core.api.exception.tryAndCatch
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
) : AsyncExecutor, OnProcessTickListener, SafeCaller {


    private var started: Boolean = false

    private val executorService: ExecutorService by lazy {
        executorServiceFactory.createSingleThreadExecutor().also {
            started = true
        }
    }

    private val mainThreadTasks = ConcurrentLinkedQueue<() -> Unit>()

    override val log = loggerFor<AsyncExecutorImpl>()

    @com.google.inject.Inject(optional = true)
    override var uncaughtExceptionNotifier: UncaughtExceptionNotifier? = null

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
            tryAndCatch {
                val task = mainThreadTasks.poll() ?: return
                task.invoke()
            }
        } while (true)
    }

    override fun execute(
            onSuccess: (() -> Unit)?,
            onFailure: ((Exception) -> Unit)?,
            action: AsyncExecutor.() -> Unit
    ) {
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

    override fun <T> compute(onSuccess: (T) -> Unit, onFailure: ((Exception) -> Unit)?, action: AsyncExecutor.() -> T) {
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
            uncaughtExceptionNotifier != null -> executeOnMainThread { handleException(e) }
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
                log.error("Exception in main thread execution", e)
                result.completeExceptionally(e)
            }
        }
        return result
    }

}