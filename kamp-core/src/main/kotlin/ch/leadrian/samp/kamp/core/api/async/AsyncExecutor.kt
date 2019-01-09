package ch.leadrian.samp.kamp.core.api.async

import java.util.concurrent.CompletableFuture

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