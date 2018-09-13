package ch.leadrian.samp.kamp.core.api.command.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class AccessChecks(val accessCheckers: Array<AccessCheck>)