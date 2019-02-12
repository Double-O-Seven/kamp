package ch.leadrian.samp.kamp.core.runtime.amx

interface AmxNativeFunctionInvoker {

    fun findNative(name: String): Int

    fun callNative(address: Int, vararg args: Int): Int

    fun invokeNative(address: Int, format: String, vararg args: Any): Int

}