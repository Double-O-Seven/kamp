package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.*

data class CommandDescription
internal constructor(
        private val text: String? = null,
        private val textKey: TextKey? = null,
        private val textProvider: TextProvider
) {

    fun getText(locale: Locale): String = when {
        textKey != null -> textProvider.getText(locale, textKey, text)
        else -> text ?: "???"
    }

}