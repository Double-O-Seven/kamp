package ch.leadrian.samp.kamp.core.api.command.annotation

import ch.leadrian.samp.kamp.core.api.command.CommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.CommandAccessDeniedHandler
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class CommandAccessChecker(
        val accessCheckers: Array<KClass<out CommandAccessChecker>>,
        val errorMessage: String = "",
        val errorMessageTextKey: String = "",
        val accessDeniedHandlers: Array<KClass<out CommandAccessDeniedHandler>> = []
)