package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.SAMPNativeFunctionHookFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class SAMPNativeFunctionExecutorProvider
@Inject
constructor(
        nativeFunctionHookFactories: Set<@JvmSuppressWildcards SAMPNativeFunctionHookFactory>,
        @Named(CoreModule.BASE_NATIVE_FUNCTION_EXECUTOR) baseNativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Provider<SAMPNativeFunctionExecutor> {

    private val nativeFunctionExecutor: SAMPNativeFunctionExecutor by lazy {
        nativeFunctionHookFactories
                .sortedByDescending { it.priority }
                .fold(baseNativeFunctionExecutor) { nativeFunctionExecutor, nativeFunctionHookFactory ->
                    nativeFunctionHookFactory.create(nativeFunctionExecutor)
                }
    }

    override fun get(): SAMPNativeFunctionExecutor = nativeFunctionExecutor

}