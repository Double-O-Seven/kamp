package ch.leadrian.samp.kamp.core.runtime.amx

interface AmxNativeFunctionInvoker {

    fun findNative(name: String): Int

    fun callNative(address: Int, args: IntArray): Int

    fun invokeNative(address: Int, format: String, args: Array<Any>): Int

}