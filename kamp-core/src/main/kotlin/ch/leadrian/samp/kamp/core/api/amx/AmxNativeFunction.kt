package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.amx.AmxNativeFunctionInvoker
import kotlin.reflect.full.safeCast

/**
 * Base class for native functions wrappers which are accessible through other plugins.
 * This class allows type-safe invocation of functions defined in ColAndreas or Incognito's Streamer plugin for example.
 *
 * Example for wrapper GetPlayerPos (which is already provided through [ch.leadrian.samp.kamp.core.api.entity.Player.coordinates]).
 * ```kotlin
 * val GetPlayerPos by AmxNativeFunction4<Int, MutableFloatCell, MutableFloatCell, MutableFloatCell>()
 * ```
 *
 * Note that there are 33 implementations available, supporting 0 up to 32 arguments.
 *
 * The following argument types are supported:
 * * [Int]: sampgdk_InvokeNative format type 'i' or 'd'
 * * [ImmutableIntCell]: sampgdk_InvokeNative format type 'r'
 * * [MutableIntCell]: sampgdk_InvokeNative format type 'R'
 * * [Float]: sampgdk_InvokeNative format type 'f'
 * * [ImmutableFloatCell]: sampgdk_InvokeNative format type 'r'
 * * [MutableFloatCell]: sampgdk_InvokeNative format type 'R'
 * * [Boolean]: sampgdk_InvokeNative format type 'b'
 * * [String]: sampgdk_InvokeNative format type 's'
 * * [OutputString]: sampgdk_InvokeNative format type 'S'
 * * [ImmutableCellArray]: sampgdk_InvokeNative format type 'a'
 * * [MutableCellArray]: sampgdk_InvokeNative format type 'A'
 *
 * @see [AmxNativeFunction0]
 * @see [AmxNativeFunction1]
 * @see [AmxNativeFunction2]
 * @see [AmxNativeFunction3]
 * @see [AmxNativeFunction4]
 * @see [AmxNativeFunction5]
 * @see [AmxNativeFunction6]
 * @see [AmxNativeFunction7]
 * @see [AmxNativeFunction8]
 * @see [AmxNativeFunction9]
 * @see [AmxNativeFunction10]
 * @see [AmxNativeFunction11]
 * @see [AmxNativeFunction12]
 * @see [AmxNativeFunction13]
 * @see [AmxNativeFunction14]
 * @see [AmxNativeFunction15]
 * @see [AmxNativeFunction16]
 * @see [AmxNativeFunction17]
 * @see [AmxNativeFunction18]
 * @see [AmxNativeFunction19]
 * @see [AmxNativeFunction20]
 * @see [AmxNativeFunction21]
 * @see [AmxNativeFunction22]
 * @see [AmxNativeFunction23]
 * @see [AmxNativeFunction24]
 * @see [AmxNativeFunction25]
 * @see [AmxNativeFunction26]
 * @see [AmxNativeFunction27]
 * @see [AmxNativeFunction28]
 * @see [AmxNativeFunction29]
 * @see [AmxNativeFunction30]
 * @see [AmxNativeFunction31]
 * @see [AmxNativeFunction32]
 */
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

        protected val nativeAddress: Int by lazy {
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
            return amxNativeFunctionInvoker.callNative(nativeAddress, intArgs)
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
            return amxNativeFunctionInvoker.invokeNative(nativeAddress, format, transformedArgs as Array<Any>)
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