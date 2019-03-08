package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.StringEncoding
import ch.leadrian.samp.kamp.core.runtime.amx.nullTerminated
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Internally used base class representing a AMX-compatible type.
 *
 * Each implementation handles serialization and deserialization into an AMX-combatible format.
 *
 * The following types are supported:
 * * [Int]: sampgdk_InvokeNative format type 'i' or 'd'
 * * [ImmutableIntCell]: sampgdk_InvokeNative format type 'r'
 * * [MutableIntCell]: sampgdk_InvokeNative format type 'R'
 * * [Float]: sampgdk_InvokeNative format type 'f'
 * * [ImmutableFloatCell]: sampgdk_InvokeNative format type 'r'
 * * [MutableFloatCell]: sampgdk_InvokeNative format type 'R'
 * * [Boolean]: sampgdk_InvokeNative format type 'b'
 * * [ImmutableBooleanCell]: sampgdk_InvokeNative format type 'r'
 * * [MutableBooleanCell]: sampgdk_InvokeNative format type 'R'
 * * [String]: sampgdk_InvokeNative format type 's'
 * * [OutputString]: sampgdk_InvokeNative format type 'S'
 * * [ImmutableCellArray]: sampgdk_InvokeNative format type 'a'
 * * [MutableCellArray]: sampgdk_InvokeNative format type 'A'
 */
sealed class AmxNativeFunctionParameterType<T : Any>(val type: KClass<T>) {

    companion object {

        inline fun <reified T : Any> get(): AmxNativeFunctionParameterType<T> = get(T::class)

        @Suppress("UNCHECKED_CAST")
        operator fun <T : Any> get(type: KClass<T>): AmxNativeFunctionParameterType<T> {
            return when (type) {
                IntType.type -> IntType as AmxNativeFunctionParameterType<T>
                ImmutableIntCellType.type -> ImmutableIntCellType as AmxNativeFunctionParameterType<T>
                MutableIntCellType.type -> MutableIntCellType as AmxNativeFunctionParameterType<T>
                FloatType.type -> FloatType as AmxNativeFunctionParameterType<T>
                ImmutableFloatCellType.type -> ImmutableFloatCellType as AmxNativeFunctionParameterType<T>
                MutableFloatCellType.type -> MutableFloatCellType as AmxNativeFunctionParameterType<T>
                BooleanType.type -> BooleanType as AmxNativeFunctionParameterType<T>
                ImmutableBooleanCellType.type -> ImmutableBooleanCellType as AmxNativeFunctionParameterType<T>
                MutableBooleanCellType.type -> MutableBooleanCellType as AmxNativeFunctionParameterType<T>
                StringType.type -> StringType as AmxNativeFunctionParameterType<T>
                OutputStringType.type -> OutputStringType as AmxNativeFunctionParameterType<T>
                ImmutableCellArrayType.type -> ImmutableCellArrayType as AmxNativeFunctionParameterType<T>
                MutableCellArrayType.type -> MutableCellArrayType as AmxNativeFunctionParameterType<T>
                else -> throw IllegalArgumentException("Unsupported type: $type")
            }
        }

    }

    internal fun tryToGetSpecifier(value: Any): String = getSpecifier(type.cast(value))

    internal abstract fun getSpecifier(value: T): String

    internal fun tryToTransformToPrimitive(value: Any): Any = transformToPrimitive(type.cast(value))

    internal abstract fun transformToPrimitive(value: T): Any

}

/**
 * Base class for simple AMX-compatible types [Int], [Float] and [Boolean].
 *
 * @see [IntType]
 * @see [FloatType]
 * @see [BooleanType]
 */
sealed class SimpleAmxNativeFunctionParameterType<T : Any>(type: KClass<T>, private val specifier: String) :
        AmxNativeFunctionParameterType<T>(type) {

    final override fun getSpecifier(value: T): String = specifier

    internal fun tryToConvertToInt(value: Any): Int = convertToInt(type.cast(value))

    internal abstract fun convertToInt(value: T): Int

    override fun transformToPrimitive(value: T): Any = intArrayOf(convertToInt(value))

}

/**
 * AMX type definition for [Int] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'i' and could also use 'd'.
 */
object IntType : SimpleAmxNativeFunctionParameterType<Int>(Int::class, "i") {

    override fun convertToInt(value: Int): Int = value

}

/**
 * AMX type definition for [ImmutableIntCell] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'r'.
 */
object ImmutableIntCellType : AmxNativeFunctionParameterType<ImmutableIntCell>(ImmutableIntCell::class) {

    override fun getSpecifier(value: ImmutableIntCell): String = "r"

    override fun transformToPrimitive(value: ImmutableIntCell): Any = value.singleElementIntArray

}

/**
 * AMX type definition for [MutableIntCell] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'R'.
 */
object MutableIntCellType : AmxNativeFunctionParameterType<MutableIntCell>(MutableIntCell::class) {

    override fun getSpecifier(value: MutableIntCell): String = "R"

    override fun transformToPrimitive(value: MutableIntCell): Any = value.singleElementIntArray

}

/**
 * AMX type definition for [Boolean] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'b'.
 */
object BooleanType : SimpleAmxNativeFunctionParameterType<Boolean>(Boolean::class, "b") {

    override fun convertToInt(value: Boolean): Int {
        return if (value) {
            1
        } else {
            0
        }
    }

}

/**
 * AMX type definition for [ImmutableBooleanCell] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'r'.
 */
object ImmutableBooleanCellType : AmxNativeFunctionParameterType<ImmutableBooleanCell>(ImmutableBooleanCell::class) {

    override fun getSpecifier(value: ImmutableBooleanCell): String = "r"

    override fun transformToPrimitive(value: ImmutableBooleanCell): Any = value.singleElementIntArray

}

/**
 * AMX type definition for [MutableBooleanCell] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'R'.
 */
object MutableBooleanCellType : AmxNativeFunctionParameterType<MutableBooleanCell>(MutableBooleanCell::class) {

    override fun getSpecifier(value: MutableBooleanCell): String = "R"

    override fun transformToPrimitive(value: MutableBooleanCell): Any = value.singleElementIntArray

}

/**
 * AMX type definition for [Float] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'f'.
 */
object FloatType : SimpleAmxNativeFunctionParameterType<Float>(Float::class, "f") {

    override fun convertToInt(value: Float): Int = value.toRawBits()

}

/**
 * AMX type definition for [ImmutableFloatCell] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'r'.
 */
object ImmutableFloatCellType : AmxNativeFunctionParameterType<ImmutableFloatCell>(ImmutableFloatCell::class) {

    override fun getSpecifier(value: ImmutableFloatCell): String = "r"

    override fun transformToPrimitive(value: ImmutableFloatCell): Any = value.singleElementIntArray

}

/**
 * AMX type definition for [MutableFloatCell] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'R'.
 */
object MutableFloatCellType : AmxNativeFunctionParameterType<MutableFloatCell>(MutableFloatCell::class) {

    override fun getSpecifier(value: MutableFloatCell): String = "R"

    override fun transformToPrimitive(value: MutableFloatCell): Any = value.singleElementIntArray

}

/**
 * AMX type definition for [String] values.
 *
 * Uses sampgdk_InvokeNative format specifier 's'.
 */
object StringType : AmxNativeFunctionParameterType<String>(String::class) {

    override fun getSpecifier(value: String): String = "s"

    override fun transformToPrimitive(value: String): Any = value.toByteArray(StringEncoding.getCharset()).nullTerminated()

}

/**
 * AMX type definition for [OutputString] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'S'.
 */
object OutputStringType : AmxNativeFunctionParameterType<OutputString>(OutputString::class) {

    override fun getSpecifier(value: OutputString): String = "S[${value.size}]"

    override fun transformToPrimitive(value: OutputString): Any = value.bytes

}

/**
 * Base AMX type definition for [CellArray] values.
 */
sealed class CellArrayType<T : CellArray>(type: KClass<T>) : AmxNativeFunctionParameterType<T>(type) {

    final override fun transformToPrimitive(value: T): Any = value.values

}

/**
 * AMX type definition for [ImmutableCellArray] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'a'.
 */
object ImmutableCellArrayType : CellArrayType<ImmutableCellArray>(ImmutableCellArray::class) {

    override fun getSpecifier(value: ImmutableCellArray): String = "a[${value.size}]"

}

/**
 * AMX type definition for [MutableCellArray] values.
 *
 * Uses sampgdk_InvokeNative format specifier 'A'.
 */
object MutableCellArrayType : CellArrayType<MutableCellArray>(MutableCellArray::class) {

    override fun getSpecifier(value: MutableCellArray): String = "A[${value.size}]"

}
