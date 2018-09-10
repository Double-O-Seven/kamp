package ch.leadrian.samp.kamp.core.runtime.text

import ch.leadrian.samp.kamp.core.api.text.HasTextKey
import ch.leadrian.samp.kamp.core.api.text.MessageArgument
import ch.leadrian.samp.kamp.core.api.text.MessageFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.*
import javax.inject.Inject

internal class MessageFormatterImpl
@Inject
constructor(private val textProvider: TextProvider) : MessageFormatter {

    override fun format(locale: Locale, color: ch.leadrian.samp.kamp.core.api.data.Color, message: String, vararg args: Any): String {
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