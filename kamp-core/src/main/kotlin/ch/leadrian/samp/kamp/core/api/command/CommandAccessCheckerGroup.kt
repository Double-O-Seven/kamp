package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.*

data class CommandAccessCheckerGroup
internal constructor(
        val accessCheckers: List<CommandAccessChecker>,
        val accessDeniedHandlers: List<CommandAccessDeniedHandler>,
        private val errorMessage: String?,
        private val errorMessageTextKey: TextKey?,
        private val textProvider: TextProvider
) {

    fun getErrorMessage(locale: Locale): String? = when {
        errorMessageTextKey != null -> textProvider.getText(locale, errorMessageTextKey, errorMessage)
        else -> errorMessage
    }

}