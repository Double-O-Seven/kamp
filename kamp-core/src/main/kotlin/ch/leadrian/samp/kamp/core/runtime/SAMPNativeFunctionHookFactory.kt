package ch.leadrian.samp.kamp.core.runtime

/**
 * Allows to create and instantiate a [SAMPNativeFunctionHook].
 * This factory must be bound inside a module through [ch.leadrian.samp.kamp.core.api.inject.KampModule.newSAMPNativeFunctionHookFactorySetBinder]
 * in order to be able to hook any other [SAMPNativeFunctionExecutor].
 * Dependencies may be injected, however, should any transitive dependency be a [SAMPNativeFunctionExecutor],
 * the dependency must be injected using a [javax.inject.Provider] to avoid cyclic dependencies.
 */
interface SAMPNativeFunctionHookFactory {

    /**
     * Created [SAMPNativeFunctionHook]s are created according to this priority.
     * The one with the highest priority value will be directly hooking the base [SAMPNativeFunctionExecutorImpl].
     * The one with the lowest priority value will be called first in the chain of [SAMPNativeFunctionHook]s.
     */
    val priority: Int

    fun create(nativeFunctionExecutor: SAMPNativeFunctionExecutor): SAMPNativeFunctionHook

}