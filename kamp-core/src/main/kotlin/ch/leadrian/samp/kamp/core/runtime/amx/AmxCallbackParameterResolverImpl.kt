package ch.leadrian.samp.kamp.core.runtime.amx

import sun.misc.Unsafe
import kotlin.reflect.KClass

internal object AmxCallbackParameterResolverImpl : AmxCallbackParameterResolver {

    private val unsafe: Unsafe = UnsafeProvider.instance

    override fun resolve(parameterTypes: List<KClass<*>>, paramsAddress: Int): Array<Any> {
        checkNumberOfParameters(paramsAddress, parameterTypes)
        val result: Array<Any?> = arrayOfNulls(parameterTypes.size)
        parameterTypes.forEachIndexed { index, type ->
            val parameterAddress = (paramsAddress + (index + 1) * Int.SIZE_BYTES).toLong()
            result[index] = when (type) {
                Int::class -> resolveInt(parameterAddress)
                Float::class -> resolveFloat(parameterAddress)
                String::class -> resolveString(parameterAddress)
                else -> throw IllegalArgumentException("Unsupported parameter type: ${type.qualifiedName}")
            }
        }
        @Suppress("UNCHECKED_CAST")
        return result as Array<Any>
    }

    private fun checkNumberOfParameters(paramsAddress: Int, parameterTypes: List<KClass<*>>) {
        val numberOfParams = unsafe.getInt(paramsAddress.toLong()) / 4
        if (numberOfParams != parameterTypes.size) {
            throw IllegalArgumentException("Expected number of parameters ${parameterTypes.size} does not match given number of parameters: $numberOfParams")
        }
    }

    private fun resolveInt(parameterAddress: Long): Int = unsafe.getInt(parameterAddress)

    private fun resolveFloat(parameterAddress: Long): Float = unsafe.getFloat(parameterAddress)

    private fun resolveString(parameterAddress: Long): String {
        val stringBuilder = StringBuilder()
        val stringAddress = unsafe.getInt(parameterAddress)
        var index = 0
        while (true) {
            val character = unsafe.getInt((stringAddress + index * Int.SIZE_BYTES).toLong())
            if (character == 0) {
                break
            }
            stringBuilder.append(character.toChar())
            index++
        }
        return stringBuilder.toString()
    }
}