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

    /**
     * Represents the [Int] value stored in the cell.
     */
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

    /**
     * Propagates the getter and setter to an internal [Int] property.
     */
    override var value: Int
        get() = intValue
        set(value) {
            intValue = value
        }

}

/**
 * Base class for [Boolean] references.
 *
 * @see [Cell]
 */
sealed class BooleanCell(value: Boolean) : Cell(if (value) 1 else 0) {

    /**
     * Represents the [Boolean] value stored in the cell.
     */
    abstract val value: Boolean

}

/**
 * Class for immutable [Boolean] references.
 *
 * @see [BooleanCell]
 * @see [ImmutableBooleanCellType]
 */
class ImmutableBooleanCell
@JvmOverloads
constructor(value: Boolean = false) : BooleanCell(value) {

    override val value: Boolean
        get() = intValue != 0

}

/**
 * Class for mutable [Boolean] references.
 *
 * @see [BooleanCell]
 * @see [MutableBooleanCellType]
 */
class MutableBooleanCell
@JvmOverloads
constructor(value: Boolean = false) : BooleanCell(value) {

    /**
     * Propagates the getter and setter to an internal [Int] property.
     */
    override var value: Boolean
        get() = intValue != 0
        set(value) {
            intValue = if (value) 1 else 0
        }

}

/**
 * Base class for [Float] references.
 *
 * @see [Cell]
 */
sealed class FloatCell(value: Float) : Cell(value.toRawBits()) {

    /**
     * Represents the [Float] value stored in the cell.
     * Internally, the [Float] value is stored in an [Int] field.
     */
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
