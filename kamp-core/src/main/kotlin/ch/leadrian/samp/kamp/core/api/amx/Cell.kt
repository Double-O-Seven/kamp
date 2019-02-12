package ch.leadrian.samp.kamp.core.api.amx

sealed class Cell(intValue: Int) {

    internal val singleElementIntArray = intArrayOf(intValue)

    protected var intValue: Int
        get() = singleElementIntArray[0]
        set(value) {
            singleElementIntArray[0] = value
        }

}

abstract class IntCell(value: Int) : Cell(value) {

    abstract val value: Int

}

class ImmutableIntCell
@JvmOverloads
constructor(value: Int = 0) : IntCell(value) {

    override val value: Int
        get() = intValue

}

class MutableIntCell
@JvmOverloads
constructor(value: Int = 0) : IntCell(value) {

    override var value: Int
        get() = intValue
        set(value) {
            intValue = value
        }

}

abstract class FloatCell(value: Float) : Cell(value.toRawBits()) {

    abstract val value: Float

}

class ImmutableFloatCell
@JvmOverloads
constructor(value: Float = 0f) : FloatCell(value) {

    override val value: Float
        get() = Float.fromBits(intValue)

}

class MutableFloatCell
@JvmOverloads
constructor(value: Float = 0f) : FloatCell(value) {

    override var value: Float
        get() = Float.fromBits(intValue)
        set(value) {
            intValue = value.toRawBits()
        }

}
