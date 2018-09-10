package ch.leadrian.samp.kamp.core.api.async

interface AsyncExecutor {

    fun execute(onSuccess: (() -> Unit)? = null, onFailure: ((Exception) -> Unit)? = null, action: AsyncExecutor.() -> Unit)

    fun <T> executeWithResult(onSuccess: (T) -> Unit, onFailure: ((Exception) -> Unit)? = null, action: AsyncExecutor.() -> T)

    fun executeOnMainThread(action: () -> Unit)

}