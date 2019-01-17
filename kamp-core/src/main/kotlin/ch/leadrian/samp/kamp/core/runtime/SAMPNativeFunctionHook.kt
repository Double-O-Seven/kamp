package ch.leadrian.samp.kamp.core.runtime

/**
 * Allows to hook any native function call on the lowest level.
 * Any hook must be instantiated through a [SAMPNativeFunctionHookFactory].
 * Injectable constructor arguments must be passed through a [javax.inject.Provider]
 * if any transitive dependencies lead to a [SAMPNativeFunctionExecutor].
 */
abstract class SAMPNativeFunctionHook(
        val hookedNativeFunctionExecutor: SAMPNativeFunctionExecutor
) : SAMPNativeFunctionExecutor by hookedNativeFunctionExecutor
