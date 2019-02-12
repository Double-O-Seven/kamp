package ch.leadrian.samp.kamp.core.api.amx

sealed class CellArray(internal val values: IntArray) {

    val size: Int = values.size

    operator fun iterator(): IntIterator = values.iterator()

    operator fun get(index: Int): Int = values[index]

}

class ImmutableCellArray(values: IntArray) : CellArray(values) {

    companion object {

        operator fun invoke(vararg values: Int): ImmutableCellArray = ImmutableCellArray(values)

    }

}

class MutableCellArray(values: IntArray) : CellArray(values) {

    companion object {

        operator fun invoke(vararg values: Int): MutableCellArray = MutableCellArray(values)

    }

    constructor(size: Int) : this(IntArray(size))

    operator fun set(index: Int, value: Int) {
        values[index] = value
    }

}
