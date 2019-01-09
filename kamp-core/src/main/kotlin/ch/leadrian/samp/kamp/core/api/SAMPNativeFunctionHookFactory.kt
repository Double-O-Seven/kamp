package ch.leadrian.samp.kamp.core.api

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

interface SAMPNativeFunctionHookFactory {

    val priority: Int

    fun create(nativeFunctionExecutor: SAMPNativeFunctionExecutor): SAMPNativeFunctionHook

}