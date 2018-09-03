package ch.leadrian.samp.kamp.api.text

import java.util.Locale

interface TextFormatter {

    fun format(locale: Locale, message: String, vararg args: Any): String
}