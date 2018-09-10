package ch.leadrian.samp.kamp.core.api.text

import java.util.*

interface TextFormatter {

    fun format(locale: Locale, message: String, vararg args: Any): String
}