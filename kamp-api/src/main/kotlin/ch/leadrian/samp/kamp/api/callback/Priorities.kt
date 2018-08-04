package ch.leadrian.samp.kamp.api.callback

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Priorities(vararg val value: Priority)