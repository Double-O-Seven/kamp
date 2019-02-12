package ch.leadrian.samp.kamp.core.runtime.amx

internal object AmxNativeFunctionInvokerImpl : AmxNativeFunctionInvoker {

    external override fun findNative(name: String): Int

    external override fun callNative(address: Int, vararg args: Int): Int

    external override fun invokeNative(address: Int, format: String, vararg args: Any): Int

}