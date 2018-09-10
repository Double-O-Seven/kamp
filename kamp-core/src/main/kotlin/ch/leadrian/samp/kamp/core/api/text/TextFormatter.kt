package ch.leadrian.samp.kamp.core.api.text

import java.util.*
import javax.inject.Inject

class TextFormatter
@Inject
internal constructor(private val textProvider: TextProvider) {

    fun format(locale: Locale, message: String, vararg args: Any): String {
        val formattedArgs = args.map {
            when (it) {
                is HasTextKey -> textProvider.getText(locale, it.textKey)
                is TextKey -> textProvider.getText(locale, it)
                else -> it
            }
        }
        return String.format(locale, message, *formattedArgs.toTypedArray())
    }
}