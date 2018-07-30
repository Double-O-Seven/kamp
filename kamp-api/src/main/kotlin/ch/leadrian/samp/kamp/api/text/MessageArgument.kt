package ch.leadrian.samp.kamp.api.text

import ch.leadrian.samp.kamp.api.data.Color
import java.util.*

interface MessageArgument {

    fun get(locale: Locale, color: Color): String

}