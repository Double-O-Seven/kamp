@file:kotlin.jvm.JvmName("TextDraws")

package ch.leadrian.samp.kamp.core.api.util

fun String.sanitizeForTextDraw(replaceWith: Char = '?'): String = this.replace('~', replaceWith)

const val MAX_TEXT_DRAW_STRING_LENGTH = 1023