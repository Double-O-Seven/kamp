package ch.leadrian.samp.kamp.core.runtime

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class SAMPNativeFunctionExecutorProvider
@Inject
constructor(
        nativeFunctionHookFactories: Set<@JvmSuppressWildcards SAMPNativeFunctionHookFactory>,
        @Named(BASE_NATIVE_FUNCTION_EXECUTOR_NAME) baseNativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Provider<SAMPNativeFunctionExecutor> {

    companion object {

        const val BASE_NATIVE_FUNCTION_EXECUTOR_NAME = "baseNativeFunctionExecutor"

    }

    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor by lazy {
        nativeFunctionHookFactories
                .sortedByDescending { it.priority }
                .fold(baseNativeFunctionExecutor) { nativeFunctionExecutor, nativeFunctionHookFactory ->
                    nativeFunctionHookFactory.create(nativeFunctionExecutor)
                }
    }

    override fun get(): SAMPNativeFunctionExecutor = nativeFunctionExecutor

}