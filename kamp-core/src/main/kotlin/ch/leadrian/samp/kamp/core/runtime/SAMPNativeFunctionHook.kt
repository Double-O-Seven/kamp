package ch.leadrian.samp.kamp.core.runtime

abstract class SAMPNativeFunctionHook(
        val hookedNativeFunctionExecutor: SAMPNativeFunctionExecutor
) : SAMPNativeFunctionExecutor by hookedNativeFunctionExecutor
