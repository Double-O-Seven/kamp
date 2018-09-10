package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import java.util.*

interface MessageArgument {

    fun get(locale: Locale, color: Color): String

}