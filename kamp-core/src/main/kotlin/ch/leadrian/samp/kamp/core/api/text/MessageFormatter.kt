package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import java.text.MessageFormat
import java.util.Locale
import javax.inject.Inject

class MessageFormatter
@Inject
internal constructor(private val textProvider: TextProvider) {

    private companion object {

        val log = loggerFor<MessageFormatter>()
    }

    fun format(locale: Locale, color: Color, textKey: TextKey, vararg args: Any): String {
        val message = textProvider.getText(locale, textKey)
        return format(locale, color, message, *args)
    }

    fun format(locale: Locale, color: Color, message: String, vararg args: Any): String {
        try {
            val formattedArgs = arrayOfNulls<Any?>(args.size)
            args.forEachIndexed { i, arg ->
                formattedArgs[i] = when (arg) {
                    is MessageArgument -> arg.getText(locale, color)
                    is Color -> arg.toEmbeddedString()
                    is HasTextKey -> textProvider.getText(locale, arg.textKey)
                    is TextKey -> textProvider.getText(locale, arg)
                    else -> arg
                }
            }
            return MessageFormat(message, locale).format(formattedArgs)
        } catch (e: Exception) {
            log.error("Error formatting message: \"{}\", args: {}", message, args.toList().toString(), e)
            return message
        }
    }
}