package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import java.util.*
import javax.inject.Inject

class MessageFormatter
@Inject
internal constructor(private val textProvider: TextProvider) {

    fun format(locale: Locale, color: Color, message: String, vararg args: Any): String {
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