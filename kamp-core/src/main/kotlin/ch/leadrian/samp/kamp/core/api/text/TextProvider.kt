package ch.leadrian.samp.kamp.core.api.text

import java.util.*

interface TextProvider {

    fun getText(locale: Locale, key: TextKey, defaultText: String? = null): String

}