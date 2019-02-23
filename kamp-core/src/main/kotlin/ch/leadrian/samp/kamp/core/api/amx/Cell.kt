package ch.leadrian.samp.kamp.core.api.amx

/**
 * Base class for references values used in sampgdk_InvokeNative.
 */
sealed class Cell(intValue: Int) {

    internal val singleElementIntArray = intArrayOf(intValue)

    protected var intValue: Int
        get() = singleElementIntArray[0]
        set(value) {
            singleElementIntArray[0] = value
        }

}

/**
 * Base class for [Int] references.
 *
 * @see [Cell]
 */
sealed class IntCell(value: Int) : Cell(value) {

    abstract val value: Int

}

/**
 * Class for immutable [Int] references.
 *
 * @see [IntCell]
 * @see [ImmutableIntCellType]
 */
class ImmutableIntCell
@JvmOverloads
constructor(value: Int = 0) : IntCell(value) {

    override val value: Int
        get() = intValue

}

/**
 * Class for mutable [Int] references.
 *
 * @see [IntCell]
 * @see [MutableIntCellType]
 */
class MutableIntCell
@JvmOverloads
constructor(value: Int = 0) : IntCell(value) {

    override var value: Int
        get() = intValue
        set(value) {
            intValue = value
        }

}

/**
 * Base class for [Float] references.
 *
 * @see [Cell]
 */
sealed class FloatCell(value: Float) : Cell(value.toRawBits()) {

    abstract val value: Float

}

/**
 * Class for immutable [Float] references.
 *
 * @see [FloatCell]
 * @see [ImmutableFloatCellType]
 */
class ImmutableFloatCell
@JvmOverloads
constructor(value: Float = 0f) : FloatCell(value) {

    override val value: Float
        get() = Float.fromBits(intValue)

}

/**
 * Class for mutable [Float] references.
 *
 * @see [FloatCell]
 * @see [MutableFloatCellType]
 */
class MutableFloatCell
@JvmOverloads
constructor(value: Float = 0f) : FloatCell(value) {

    override var value: Float
        get() = Float.fromBits(intValue)
        set(value) {
            intValue = value.toRawBits()
        }

}
