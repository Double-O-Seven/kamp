package ch.leadrian.samp.kamp.runtime.entity.interceptor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class InterceptorPriority(val value: Int)

val Any.interceptorPriority: Int
    get() = this::class.java.getAnnotation(InterceptorPriority::class.java)?.value ?: 0
