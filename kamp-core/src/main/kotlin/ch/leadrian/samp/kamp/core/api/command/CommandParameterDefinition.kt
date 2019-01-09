package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.*

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

    fun getName(locale: Locale): String = when {
        nameTextKey != null -> textProvider.getText(locale, nameTextKey, name)
        else -> name ?: type.simpleName
    }

    fun getDescription(locale: Locale): String? = when {
        descriptionTextKey != null -> textProvider.getText(locale, descriptionTextKey, description)
        else -> description
    }

}