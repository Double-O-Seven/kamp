package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import java.util.Locale

interface MessageArgument {

    fun getText(locale: Locale, color: Color): String

}