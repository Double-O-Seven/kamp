package ch.leadrian.samp.kamp.view

interface ViewDimension {

    fun getValue(parentValue: Float): Float

}

private data class Absolute(val value: Float) : ViewDimension {

    override fun getValue(parentValue: Float) = value
}

private data class RelativeToParent(val relativeValue: Float) : ViewDimension {

    override fun getValue(parentValue: Float): Float = relativeValue * parentValue / 100f

}

fun Number.pixels(): ViewDimension = Absolute(this.toFloat())

inline fun pixels(crossinline computation: (Float) -> Float): ViewDimension = object : ViewDimension {

    override fun getValue(parentValue: Float): Float = computation.invoke(parentValue)

}

fun Number.percent(): ViewDimension = RelativeToParent(this.toFloat())

inline fun percent(crossinline computation: (Float) -> Float): ViewDimension = object : ViewDimension {

    override fun getValue(parentValue: Float): Float = computation(parentValue) * parentValue / 100f

}
