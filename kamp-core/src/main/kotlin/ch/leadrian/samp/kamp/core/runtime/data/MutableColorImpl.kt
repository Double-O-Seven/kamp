package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.MutableColor

internal data class MutableColorImpl(override var value: Int) : MutableColor {

    override var r: Int
        get() = (value shr 24) and 0xFF
        set(value) {
            this.value = (this.value and 0x00FFFFFF) or ((value and 0xFF) shl 24)
        }

    override var g: Int
        get() = (value shr 16) and 0xFF
        set(value) {
            this.value = (this.value and 0xFF00FFFF.toInt()) or ((value and 0xFF) shl 16)
        }

    override var b: Int
        get() = (value shr 8) and 0xFF
        set(value) {
            this.value = (this.value and 0xFFFF00FF.toInt()) or ((value and 0xFF) shl 8)
        }

    override var a: Int
        get() = value and 0xFF
        set(value) {
            this.value = (this.value and 0xFFFFFF00.toInt()) or (value and 0xFF)
        }

    override var rgb: Int
        get() = (value shr 8) and 0xFFFFFF
        set(value) {
            this.value = (this.value and 0xFF) or ((value and 0xFFFFFF) shl 8)
        }

    override var argb: Int
        get() = ((value and 0xFF) shl 24) or rgb
        set(value) {
            this.value = ((value and 0xFFFFFF) shl 8) or ((value shr 24) and 0xFF)
        }

    override fun toHexString(): String = String.format("%08x", value)

    override fun toEmbeddedString(): String = String.format("{%06x}", rgb)

    override fun toColor(): Color = ColorImpl(
            value
    )

    override fun toMutableColor(): MutableColor = this
}