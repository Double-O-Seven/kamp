package ch.leadrian.samp.kamp.core.api.text

import java.util.*

interface MessageArgument {

    fun get(locale: Locale, color: ch.leadrian.samp.kamp.core.api.data.Color): String

}