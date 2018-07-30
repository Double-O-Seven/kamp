package ch.leadrian.samp.kamp.api.text

import ch.leadrian.samp.kamp.api.data.Color
import java.util.*
import kotlin.collections.HashMap

abstract class Translatable : MessageArgument {

    private val translations = HashMap<Locale, String>()

    override fun get(locale: Locale, color: Color): String =
            translations.computeIfAbsent(locale) { get(locale) }

    abstract fun get(locale: Locale): String
}