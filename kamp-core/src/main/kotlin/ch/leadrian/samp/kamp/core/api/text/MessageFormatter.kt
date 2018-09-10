package ch.leadrian.samp.kamp.core.api.text

import java.util.*

interface MessageFormatter {

    fun format(locale: Locale, color: ch.leadrian.samp.kamp.core.api.data.Color, message: String, vararg args: Any): String
}