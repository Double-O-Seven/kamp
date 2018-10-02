package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import java.text.MessageFormat
import java.util.*
import javax.inject.Inject

class MessageFormatter
@Inject
internal constructor(private val textProvider: TextProvider) {

    fun format(locale: Locale, color: Color, message: String, vararg args: Any): String {
        val formattedArgs = arrayOfNulls<Any?>(args.size)
        args.forEachIndexed { i, arg ->
            formattedArgs[i] = when (arg) {
                is MessageArgument -> arg.get(locale, color)
                is Color -> arg.toEmbeddedString()
                is HasTextKey -> textProvider.getText(locale, arg.textKey)
                is TextKey -> textProvider.getText(locale, arg)
                else -> arg
            }
        }
        return MessageFormat(message, locale).format(formattedArgs)
    }
}