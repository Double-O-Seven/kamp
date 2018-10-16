package ch.leadrian.samp.kamp.core.api.data

interface Color {

    val value: Int

    val r: Int

    val g: Int

    val b: Int

    val a: Int

    val rgb: Int

    val argb: Int

    fun toHexString(): String = String.format("%08x", value)

    fun toEmbeddedString(): String = String.format("{%06x}", rgb)

    fun toColor(): Color

    fun toMutableColor(): MutableColor
}