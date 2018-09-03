package ch.leadrian.samp.kamp.runtime.text

import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.text.HasTextKey
import ch.leadrian.samp.kamp.api.text.MessageArgument
import ch.leadrian.samp.kamp.api.text.MessageFormatter
import ch.leadrian.samp.kamp.api.text.TextKey
import ch.leadrian.samp.kamp.api.text.TextProvider
import java.util.Locale
import javax.inject.Inject

internal class MessageFormatterImpl
@Inject
constructor(private val textProvider: TextProvider) : MessageFormatter {

    override fun format(locale: Locale, color: Color, message: String, vararg args: Any): String {
        val formattedArgs = args.map {
            when (it) {
                is MessageArgument -> it.get(locale, color)
                is HasTextKey -> textProvider.getText(locale, it.textKey)
                is TextKey -> textProvider.getText(locale, it)
                else -> it
            }
        }
        return String.format(locale, message, *formattedArgs.toTypedArray())
    }
}