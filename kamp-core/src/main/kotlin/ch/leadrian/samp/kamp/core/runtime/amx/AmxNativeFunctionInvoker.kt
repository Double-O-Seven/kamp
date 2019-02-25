package ch.leadrian.samp.kamp.core.runtime.amx

internal interface AmxNativeFunctionInvoker {

    fun findNative(name: String): Int

    fun callNative(nativeAddress: Int, args: IntArray): Int

    fun invokeNative(nativeAddress: Int, format: String, args: Array<Any>): Int

}