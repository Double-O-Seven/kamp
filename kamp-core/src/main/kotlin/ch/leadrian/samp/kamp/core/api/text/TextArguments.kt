package ch.leadrian.samp.kamp.core.api.text

import java.util.*

private class TextTranslatable(private val translator: (Locale) -> String) : TextArgument {

    override fun get(locale: Locale): String = translator(locale)

}

fun translateForText(translator: (Locale) -> String): TextArgument = TextTranslatable(translator)