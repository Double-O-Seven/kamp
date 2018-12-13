package ch.leadrian.samp.kamp.view.base

import java.util.*

interface TextTransformer {

    fun transform(text: String, locale: Locale): String

}