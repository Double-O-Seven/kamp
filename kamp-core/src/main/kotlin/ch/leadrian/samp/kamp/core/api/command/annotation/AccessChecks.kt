package ch.leadrian.samp.kamp.core.api.command.annotation

/**
 * Defines multiple [AccessCheck]s that will be applied in the defined order.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class AccessChecks(val accessChecks: Array<AccessCheck>)