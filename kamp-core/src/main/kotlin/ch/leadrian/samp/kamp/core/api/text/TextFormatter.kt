package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.util.loggerFor
import java.text.MessageFormat
import java.util.*
import javax.inject.Inject

class TextFormatter
@Inject
internal constructor(private val textProvider: TextProvider) {

    private companion object {

        val log = loggerFor<TextFormatter>()
    }

    fun format(locale: Locale, message: String, vararg args: Any): String {
        return try {
            val formattedArgs = arrayOfNulls<Any?>(args.size)
            args.forEachIndexed { i, arg ->
                formattedArgs[i] = when (arg) {
                    is TextArgument -> arg.getText(locale)
                    is HasTextKey -> textProvider.getText(locale, arg.textKey)
                    is TextKey -> textProvider.getText(locale, arg)
                    else -> arg
                }
            }
            MessageFormat(message, locale).format(formattedArgs)
        } catch (e: Exception) {
            log.error("Error formatting message: \"{}\", args: {}", message, args.toList().toString(), e)
            message
        }
    }
}