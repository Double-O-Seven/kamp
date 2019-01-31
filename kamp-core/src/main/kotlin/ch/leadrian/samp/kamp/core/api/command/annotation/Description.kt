package ch.leadrian.samp.kamp.core.api.command.annotation

/**
 * Add a description for a command. The description can be access through a [ch.leadrian.samp.kamp.core.api.command.CommandDefinition].
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Description(
        val text: String = "",
        val textKey: String = ""
)