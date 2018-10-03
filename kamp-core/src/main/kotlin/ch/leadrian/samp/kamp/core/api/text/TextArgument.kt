package ch.leadrian.samp.kamp.core.api.text

import java.util.*

interface TextArgument {

    fun get(locale: Locale): String

}