package ch.leadrian.samp.kamp.core.api.async

import ch.leadrian.samp.kamp.core.runtime.async.AsyncExecutorImpl
import com.google.inject.ImplementedBy
import java.util.concurrent.CompletableFuture

/**
 * A helper class that allows to execute on a thread other than the main thread.
 * Use this to execute computationally intensive code, such as path finding for a GPS for example.
 * *Be aware that you may NOT execute any native functions! Those maybe only be executed on the main thread!*
 *
 * Each asynchronous action will be sequentially executed by a single worker thread in the background.
 *
 * @see [ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor]
 */
@ImplementedBy(AsyncExecutorImpl::class)
interface AsyncExecutor {

    /**
     * Executes a simple [action] in the background.
     * If no exception was thrown during execution, [onSuccess] will be executed on the main thread during the next SA-MP server tick.
     * If an exception was thrown during execution, [onFailure] will be executed on the main thread during the next SA-MP server tick,
     * with the throw exception passed as parameter.
     */
    fun execute(
            onSuccess: (() -> Unit)? = null,
            onFailure: ((Exception) -> Unit)? = null,
            action: AsyncExecutor.() -> Unit
    )

    /**
     * Computes a value of type [T] in the background.
     * If no exception was thrown during execution, [onSuccess] will be executed on the main thread during the next SA-MP server tick,
     * with the computed value passed as parameter.
     * If an exception was thrown during execution, [onFailure] will be executed on the main thread during the next SA-MP server tick,
     * with the throw exception passed as parameter.
     */
    fun <T> compute(onSuccess: (T) -> Unit, onFailure: ((Exception) -> Unit)? = null, action: AsyncExecutor.() -> T)

    /**
     * If the function invocation is performed on the main thread, [action] will be executed immediately,
     * otherwise, action will be executed on the main thread.
     */
    fun executeOnMainThread(action: () -> Unit)

    /**
     * If the function invocation is performed on the main thread, [action] will be executed immediately,
     * otherwise, action will be executed on the main thread.
     *
     * @return a [CompletableFuture] containing the computed value, if no exception was thrown, or else the thrown exception
     */
    fun <T> computeOnMainThread(action: () -> T): CompletableFuture<T>

}