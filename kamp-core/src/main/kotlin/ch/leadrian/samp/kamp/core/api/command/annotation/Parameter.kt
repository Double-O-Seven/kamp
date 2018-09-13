package ch.leadrian.samp.kamp.core.api.command.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Parameter(
        val name: String = "",
        val nameTextKey: String = "",
        val description: String = "",
        val descriptionTextKey: String = ""
)