package ch.leadrian.samp.kamp.api.text

import ch.leadrian.samp.kamp.api.data.Color
import java.util.Locale

interface MessageFormatter {

    fun format(locale: Locale, color: Color, message: String, vararg args: Any): String
}