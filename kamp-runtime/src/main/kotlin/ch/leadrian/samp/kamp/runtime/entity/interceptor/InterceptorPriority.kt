package ch.leadrian.samp.kamp.runtime.entity.interceptor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class InterceptorPriority(val value: Int)
