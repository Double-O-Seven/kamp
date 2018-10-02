package ch.leadrian.samp.kamp.core.api.text

import java.text.MessageFormat
import java.util.*
import javax.inject.Inject

class TextFormatter
@Inject
internal constructor(private val textProvider: TextProvider) {

    fun format(locale: Locale, message: String, vararg args: Any): String {
        val formattedArgs = arrayOfNulls<Any?>(args.size)
        args.forEachIndexed { i, arg ->
            formattedArgs[i] = when (arg) {
                is HasTextKey -> textProvider.getText(locale, arg.textKey)
                is TextKey -> textProvider.getText(locale, arg)
                else -> arg
            }
        }
        return MessageFormat(message, locale).format(formattedArgs)
    }
}