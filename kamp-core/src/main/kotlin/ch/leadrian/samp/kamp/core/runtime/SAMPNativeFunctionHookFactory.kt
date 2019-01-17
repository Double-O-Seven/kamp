package ch.leadrian.samp.kamp.core.runtime

interface SAMPNativeFunctionHookFactory {

    val priority: Int

    fun create(nativeFunctionExecutor: SAMPNativeFunctionExecutor): SAMPNativeFunctionHook

}