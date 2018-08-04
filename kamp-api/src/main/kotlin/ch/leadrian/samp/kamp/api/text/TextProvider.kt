package ch.leadrian.samp.kamp.api.text

import java.util.*

interface TextProvider {

    fun getText(locale: Locale, key: TextKey, defaultText: String? = null)

}