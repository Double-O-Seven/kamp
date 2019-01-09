package ch.leadrian.samp.kamp.core.api.text

import java.util.Locale

interface TextArgument {

    fun getText(locale: Locale): String

}