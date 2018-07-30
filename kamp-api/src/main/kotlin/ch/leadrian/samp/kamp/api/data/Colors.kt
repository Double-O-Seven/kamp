@file:kotlin.jvm.JvmName("Colors")

package ch.leadrian.samp.kamp.api.data

fun colorOf(value: Int): Color = ColorImpl(value)

fun colorOf(r: Int, g: Int, b: Int, a: Int): Color = ColorImpl(rgbaValue(r = r, g = g, b = b, a = a))

fun mutableColorOf(value: Int): MutableColor = MutableColorImpl(value)

fun mutableColorOf(r: Int, g: Int, b: Int, a: Int): MutableColor = MutableColorImpl(rgbaValue(r = r, g = g, b = b, a = a))

private fun rgbaValue(r: Int, g: Int, b: Int, a: Int) =
        ((r and 0xFF) shl 24) or ((g and 0xFF) shl 16) or ((b and 0xFF) shl 8) or (a and 0xFF)
