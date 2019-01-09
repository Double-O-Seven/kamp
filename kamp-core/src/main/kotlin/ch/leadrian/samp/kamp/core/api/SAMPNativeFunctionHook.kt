package ch.leadrian.samp.kamp.core.api

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

abstract class SAMPNativeFunctionHook(
        val hookedNativeFunctionExecutor: SAMPNativeFunctionExecutor
) : SAMPNativeFunctionExecutor by hookedNativeFunctionExecutor
