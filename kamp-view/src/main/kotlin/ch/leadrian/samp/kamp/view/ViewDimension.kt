package ch.leadrian.samp.kamp.view

interface ViewDimension {

    fun getValue(parentValue: Float): Float

}

private class Absolute(val value: Float) : ViewDimension {

    override fun getValue(parentValue: Float) = value
}

private class RelativeToParent(val relativeValue: Float) : ViewDimension {

    override fun getValue(parentValue: Float): Float = relativeValue * parentValue

}

fun percent(value: Float): ViewDimension = RelativeToParent(value.toFloat())

fun Number.percent(): ViewDimension = RelativeToParent(this.toFloat())

fun pixels(value: Float): ViewDimension = Absolute(value.toFloat())

fun Number.pixels(): ViewDimension = Absolute(this.toFloat())

inline fun compute(crossinline computation: (Float) -> Float): ViewDimension = object : ViewDimension {

    override fun getValue(parentValue: Float): Float = computation(parentValue)

}
