package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.Locale

/**
 * A collection of [CommandAccessChecker] applied on a [CommandDefinition].
 */
data class CommandAccessCheckerGroup
internal constructor(
        val accessCheckers: List<CommandAccessChecker>,
        val accessDeniedHandlers: List<CommandAccessDeniedHandler> = emptyList(),
        private val errorMessage: String? = null,
        private val errorMessageTextKey: TextKey? = null,
        private val textProvider: TextProvider
) {

    /**
     * May only be used if no [CommandAccessDeniedHandler] processed the command access denial.
     *
     * @return the error message that should be displayed to the player in case command access was denied
     */
    fun getErrorMessage(locale: Locale): String? = when {
        errorMessageTextKey != null -> textProvider.getText(locale, errorMessageTextKey, errorMessage)
        else -> errorMessage
    }

}