package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.StringEncoding
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

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
                StringType.type -> StringType as AmxNativeFunctionParameterType<T>
                OutputStringType.type -> OutputStringType as AmxNativeFunctionParameterType<T>
                ImmutableCellArrayType.type -> ImmutableCellArrayType as AmxNativeFunctionParameterType<T>
                MutableCellArrayType.type -> MutableCellArrayType as AmxNativeFunctionParameterType<T>
                else -> throw UnsupportedOperationException("Unsupported type: $type")
            }
        }

    }

    fun tryToGetSpecifier(value: Any): String = getSpecifier(type.cast(value))

    abstract fun getSpecifier(value: T): String

    fun tryToTransformToPrimitive(value: Any): Any = transformToPrimitive(type.cast(value))

    open fun transformToPrimitive(value: T): Any = value

}

sealed class SimpleAmxNativeFunctionParameterType<T : Any>(type: KClass<T>, private val specifier: String) :
        AmxNativeFunctionParameterType<T>(type) {

    final override fun getSpecifier(value: T): String = specifier

    fun tryToConvertToInt(value: Any): Int = convertToInt(type.cast(value))

    abstract fun convertToInt(value: T): Int

}

object IntType : SimpleAmxNativeFunctionParameterType<Int>(Int::class, "i") {

    override fun convertToInt(value: Int): Int = value

}

object ImmutableIntCellType : AmxNativeFunctionParameterType<ImmutableIntCell>(ImmutableIntCell::class) {

    override fun getSpecifier(value: ImmutableIntCell): String = "r"

    override fun transformToPrimitive(value: ImmutableIntCell): Any = value.singleElementIntArray

}

object MutableIntCellType : AmxNativeFunctionParameterType<MutableIntCell>(MutableIntCell::class) {

    override fun getSpecifier(value: MutableIntCell): String = "R"

    override fun transformToPrimitive(value: MutableIntCell): Any = value.singleElementIntArray

}

object BooleanType : SimpleAmxNativeFunctionParameterType<Boolean>(Boolean::class, "b") {

    override fun convertToInt(value: Boolean): Int {
        return if (value) {
            1
        } else {
            0
        }
    }

}

object FloatType : SimpleAmxNativeFunctionParameterType<Float>(Float::class, "f") {

    override fun convertToInt(value: Float): Int = value.toRawBits()

}

object ImmutableFloatCellType : AmxNativeFunctionParameterType<ImmutableFloatCell>(ImmutableFloatCell::class) {

    override fun getSpecifier(value: ImmutableFloatCell): String = "r"

    override fun transformToPrimitive(value: ImmutableFloatCell): Any = value.singleElementIntArray

}

object MutableFloatCellType : AmxNativeFunctionParameterType<MutableFloatCell>(MutableFloatCell::class) {

    override fun getSpecifier(value: MutableFloatCell): String = "R"

    override fun transformToPrimitive(value: MutableFloatCell): Any = value.singleElementIntArray

}

object StringType : AmxNativeFunctionParameterType<String>(String::class) {

    override fun getSpecifier(value: String): String = "s"

    override fun transformToPrimitive(value: String): Any = value.toByteArray(StringEncoding.getCharset())

}

object OutputStringType : AmxNativeFunctionParameterType<OutputString>(OutputString::class) {

    override fun getSpecifier(value: OutputString): String = "S[${value.size}]"

    override fun transformToPrimitive(value: OutputString): Any = value.bytes

}

sealed class CellArrayType<T : CellArray>(type: KClass<T>) : AmxNativeFunctionParameterType<T>(type) {

    final override fun transformToPrimitive(value: T): Any = value.values

}

object ImmutableCellArrayType : CellArrayType<ImmutableCellArray>(ImmutableCellArray::class) {

    override fun getSpecifier(value: ImmutableCellArray): String = "a[${value.size}]"

}

object MutableCellArrayType : CellArrayType<MutableCellArray>(MutableCellArray::class) {

    override fun getSpecifier(value: MutableCellArray): String = "A[${value.size}]"

}
