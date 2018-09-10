package ch.leadrian.samp.kamp.core.api.data

internal data class ColorImpl(override val value: Int) : ch.leadrian.samp.kamp.core.api.data.Color {

    override val r: Int = (value shr 24) and 0xFF

    override val g: Int = (value shr 16) and 0xFF

    override val b: Int = (value shr 8) and 0xFF

    override val a: Int = value and 0xFF

    override val rgb: Int = (value shr 8) and 0xFFFFFF

    override fun toColor(): ch.leadrian.samp.kamp.core.api.data.Color = this

    override fun toMutableColor(): MutableColor = MutableColorImpl(value)
}