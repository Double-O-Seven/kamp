package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.amx.AmxNativeFunctionInvoker
import kotlin.reflect.full.safeCast

abstract class AmxNativeFunction internal constructor(
        name: String,
        amxNativeFunctionInvoker: AmxNativeFunctionInvoker,
        vararg parameterTypes: AmxNativeFunctionParameterType<*>
) {

    private val numberOfParameters = parameterTypes.size

    private val invocation: Invocation by lazy {
        val simpleParameterTypes: List<SimpleAmxNativeFunctionParameterType<*>> = parameterTypes.mapNotNull {
            SimpleAmxNativeFunctionParameterType::class.safeCast(it)
        }
        if (simpleParameterTypes.size == parameterTypes.size) {
            SimpleInvocation(name, amxNativeFunctionInvoker, simpleParameterTypes)
        } else {
            ComplexInvocation(name, amxNativeFunctionInvoker, parameterTypes.toList())
        }
    }

    internal fun invokeInternal(vararg args: Any): Int {
        check(args.size == numberOfParameters) {
            "Number of arguments ${args.size} does not match expected $numberOfParameters"
        }
        return invocation(*args)
    }

    private abstract class Invocation(functionName: String, val amxNativeFunctionInvoker: AmxNativeFunctionInvoker) {

        protected val address: Int by lazy {
            amxNativeFunctionInvoker
                    .findNative(functionName)
                    .takeIf { it != 0 }
                    ?: throw IllegalStateException("Could not find native with name '$functionName'")
        }

        abstract operator fun invoke(vararg args: Any): Int

    }

    private class SimpleInvocation(
            functionName: String,
            amxNativeFunctionInvoker: AmxNativeFunctionInvoker,
            private val parameterTypes: List<SimpleAmxNativeFunctionParameterType<*>>
    ) : Invocation(functionName, amxNativeFunctionInvoker) {

        override fun invoke(vararg args: Any): Int {
            val intArgs = IntArray(args.size)
            args.forEachIndexed { i, arg ->
                intArgs[i] = parameterTypes[i].tryToConvertToInt(arg)
            }
            return amxNativeFunctionInvoker.callNative(address, intArgs)
        }

    }

    private class ComplexInvocation(
            functionName: String,
            amxNativeFunctionInvoker: AmxNativeFunctionInvoker,
            private val parameterTypes: List<AmxNativeFunctionParameterType<*>>
    ) : Invocation(functionName, amxNativeFunctionInvoker) {

        override fun invoke(vararg args: Any): Int {
            val transformedArgs = arrayOfNulls<Any>(args.size)
            args.forEachIndexed { i, arg ->
                transformedArgs[i] = parameterTypes[i].tryToTransformToPrimitive(arg)
            }
            val format = getFormat(*args)
            @Suppress("UNCHECKED_CAST")
            return amxNativeFunctionInvoker.invokeNative(address, format, transformedArgs as Array<Any>)
        }

        private fun getFormat(vararg args: Any): String {
            val formatBuilder = StringBuilder()
            args.forEachIndexed { index, arg ->
                formatBuilder.append(parameterTypes[index].tryToGetSpecifier(arg))
            }
            return formatBuilder.toString()
        }

    }
}