package ch.leadrian.samp.kamp.core.runtime.amx

import sun.misc.Unsafe

internal object AmxNativeFunctionInvokerImpl : AmxNativeFunctionInvoker {

    private val unsafe: Unsafe = UnsafeProvider.instance

    external override fun findNative(name: String): Int

    external override fun callNative(nativeAddress: Int, args: IntArray): Int

    override fun invokeNative(nativeAddress: Int, format: String, args: Array<Any>): Int {
        val intArgs = IntArray(args.size)
        args.forEachIndexed { i, arg ->
            intArgs[i] = when (arg) {
                is IntArray -> arg.dataMemoryAddress
                is ByteArray -> arg.dataMemoryAddress
                else -> throw IllegalArgumentException("Unsupported argument type: ${arg::class.qualifiedName}")
            }
        }
        return invokeNative(nativeAddress, format, intArgs.dataMemoryAddress)
    }

    private external fun invokeNative(nativeAddress: Int, format: String, argsAddress: Int): Int

    private val Any.memoryAddress: Int
        get() {
            val holder = arrayOf(this)
            return unsafe.getInt(holder, Unsafe.ARRAY_OBJECT_BASE_OFFSET.toLong())
        }

    private val IntArray.dataMemoryAddress: Int
        get() = memoryAddress + Unsafe.ARRAY_INT_BASE_OFFSET

    private val ByteArray.dataMemoryAddress: Int
        get() = memoryAddress + Unsafe.ARRAY_BYTE_BASE_OFFSET

}