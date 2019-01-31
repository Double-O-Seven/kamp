package ch.leadrian.samp.kamp.core.api.command.annotation

/**
 * Names and describes a parameter of a [Command].
 *
 * If [nameTextKey] is not empty, the parameter name will be translated,
 * else, if [name] is not empty, it will be used as parameter name,
 * else the name will be the simple name of the parameter type.
 *
 * If [descriptionTextKey] is not empty, the parameter description will be translated,
 * else, if [description] is not empty, it will be used as parameter description,
 * else, not description will be available.
 * If a description is available, it will be access through [ch.leadrian.samp.kamp.core.api.command.CommandDefinition.description].
 *
 * @see [ch.leadrian.samp.kamp.core.api.command.CommandDescription]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Parameter(
        val name: String = "",
        val nameTextKey: String = "",
        val description: String = "",
        val descriptionTextKey: String = ""
)