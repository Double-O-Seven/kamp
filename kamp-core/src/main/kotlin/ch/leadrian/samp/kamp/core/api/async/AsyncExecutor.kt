package ch.leadrian.samp.kamp.core.api.async

import ch.leadrian.samp.kamp.core.runtime.async.AsyncExecutorImpl
import com.google.inject.ImplementedBy
import java.util.concurrent.CompletableFuture

@ImplementedBy(AsyncExecutorImpl::class)
interface AsyncExecutor {

    fun execute(
            onSuccess: (() -> Unit)? = null,
            onFailure: ((Exception) -> Unit)? = null,
            action: AsyncExecutor.() -> Unit
    )

    fun <T> compute(onSuccess: (T) -> Unit, onFailure: ((Exception) -> Unit)? = null, action: AsyncExecutor.() -> T)

    fun executeOnMainThread(action: () -> Unit)

    fun <T> computeOnMainThread(action: () -> T): CompletableFuture<T>

}