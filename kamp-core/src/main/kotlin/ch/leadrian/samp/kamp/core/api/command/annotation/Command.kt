package ch.leadrian.samp.kamp.core.api.command.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Command(
        val name: String = "",
        val aliases: Array<String> = [],
        val isGreedy: Boolean = true
)