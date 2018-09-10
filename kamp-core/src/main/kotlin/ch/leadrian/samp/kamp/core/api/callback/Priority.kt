package ch.leadrian.samp.kamp.core.api.callback

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Priority(
        val value: Int,
        val listenerClass: KClass<*>
)