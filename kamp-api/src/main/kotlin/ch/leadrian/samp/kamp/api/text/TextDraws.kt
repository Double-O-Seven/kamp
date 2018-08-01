@file:kotlin.jvm.JvmName("TextDraws")

package ch.leadrian.samp.kamp.api.text

fun String.sanitizeForTextDraw(replaceWith: Char = '?'): String = this.replace('~', replaceWith)