package ch.leadrian.samp.kamp.core.api.command.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Description(
        val text: String = "",
        val textKey: String = ""
)