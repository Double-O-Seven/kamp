package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.Locale

/**
 * Data class containing various information about a certain command parameter, including it's [type] and appropriate [CommandParameterResolver], name and description.
 *
 * @see [InvalidCommandParameterValueHandler]
 */
data class CommandParameterDefinition
internal constructor(
        val type: Class<*>,
        val resolver: CommandParameterResolver<*>,
        val invalidCommandParameterValueHandler: InvalidCommandParameterValueHandler? = null,
        private val name: String? = null,
        private val nameTextKey: TextKey? = null,
        private val description: String? = null,
        private val descriptionTextKey: TextKey? = null,
        private val textProvider: TextProvider
) {

    /**
     * @return the name of a parameter
     *
     * @see [ch.leadrian.samp.kamp.core.api.command.annotation.Parameter]
     */
    fun getName(locale: Locale): String = when {
        nameTextKey != null -> textProvider.getText(locale, nameTextKey, name)
        else -> name ?: type.simpleName
    }

    /**
     * @return the description of a parameter
     *
     * @see [ch.leadrian.samp.kamp.core.api.command.annotation.Parameter]
     */
    fun getDescription(locale: Locale): String? = when {
        descriptionTextKey != null -> textProvider.getText(locale, descriptionTextKey, description)
        else -> description
    }

}