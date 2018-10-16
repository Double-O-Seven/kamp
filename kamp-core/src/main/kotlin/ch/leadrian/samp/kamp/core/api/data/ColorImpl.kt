package ch.leadrian.samp.kamp.core.api.data

internal data class ColorImpl(override val value: Int) : Color {

    override val r: Int = (value shr 24) and 0xFF

    override val g: Int = (value shr 16) and 0xFF

    override val b: Int = (value shr 8) and 0xFF

    override val a: Int = value and 0xFF

    override val rgb: Int = (value shr 8) and 0xFFFFFF

    override val argb: Int = ((value and 0xFF) shl 24) or rgb

    override fun toColor(): Color = this

    override fun toMutableColor(): MutableColor = MutableColorImpl(value)
}