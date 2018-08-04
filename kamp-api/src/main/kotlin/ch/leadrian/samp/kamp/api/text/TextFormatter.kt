package ch.leadrian.samp.kamp.api.text

import java.util.*

interface TextFormatter {

    fun format(locale: Locale, message: String, vararg args: Any): String
}