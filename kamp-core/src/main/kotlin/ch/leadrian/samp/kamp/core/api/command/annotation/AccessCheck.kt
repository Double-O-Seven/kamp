package ch.leadrian.samp.kamp.core.api.command.annotation

import ch.leadrian.samp.kamp.core.api.command.CommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.CommandAccessDeniedHandler
import kotlin.reflect.KClass

/**
 * Defines one or more [CommandAccessChecker]s that will be applied to the [ch.leadrian.samp.kamp.core.api.command.Commands] class
 * or command method. Access will be denied as soon as the first [CommandAccessChecker.hasAccess] returns false.
 *
 * If [errorMessageTextKey] is not empty, the player will receive the translated message,
 * else, if [errorMessage] is not empty, the player will receive the message directly,
 * else, the [accessDeniedHandlers] was be applied until one returns [ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener.Result.Processed].
 *
 * @see [CommandAccessChecker]
 * @see [CommandAccessDeniedHandler]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class AccessCheck(
        val accessCheckers: Array<KClass<out CommandAccessChecker>>,
        val errorMessage: String = "",
        val errorMessageTextKey: String = "",
        val accessDeniedHandlers: Array<KClass<out CommandAccessDeniedHandler>> = []
)