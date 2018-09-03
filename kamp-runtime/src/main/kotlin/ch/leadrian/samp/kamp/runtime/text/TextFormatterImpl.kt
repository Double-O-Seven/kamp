package ch.leadrian.samp.kamp.runtime.text

import ch.leadrian.samp.kamp.api.text.HasTextKey
import ch.leadrian.samp.kamp.api.text.TextFormatter
import ch.leadrian.samp.kamp.api.text.TextKey
import ch.leadrian.samp.kamp.api.text.TextProvider
import java.util.Locale
import javax.inject.Inject

internal class TextFormatterImpl
@Inject
constructor(private val textProvider: TextProvider) : TextFormatter {

    override fun format(locale: Locale, message: String, vararg args: Any): String {
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