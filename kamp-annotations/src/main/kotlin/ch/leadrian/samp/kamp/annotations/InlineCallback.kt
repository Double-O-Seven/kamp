package ch.leadrian.samp.kamp.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class InlineCallback(val name: String)
