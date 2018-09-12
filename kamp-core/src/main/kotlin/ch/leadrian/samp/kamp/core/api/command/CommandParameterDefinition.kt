package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.*

data class CommandParameterDefinition
internal constructor(
        val type: Class<out Any>,
        val resolver: CommandParameterResolver<out Any>,
        val invalidCommandParameterValueHandler: InvalidCommandParameterValueHandler?,
        private val name: String?,
        private val nameTextKey: TextKey?,
        private val description: String?,
        private val descriptionTextKey: TextKey?,
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