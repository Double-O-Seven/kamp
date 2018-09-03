package ch.leadrian.samp.kamp.api.text

import ch.leadrian.samp.kamp.api.data.Color
import java.util.Locale

interface MessageArgument {

    fun get(locale: Locale, color: Color): String

}