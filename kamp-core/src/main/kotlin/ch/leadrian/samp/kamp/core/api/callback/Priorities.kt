package ch.leadrian.samp.kamp.core.api.callback

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Priorities(vararg val value: ch.leadrian.samp.kamp.core.api.callback.Priority)