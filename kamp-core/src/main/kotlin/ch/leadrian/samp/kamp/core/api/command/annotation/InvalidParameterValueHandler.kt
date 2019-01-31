package ch.leadrian.samp.kamp.core.api.command.annotation

import ch.leadrian.samp.kamp.core.api.command.InvalidCommandParameterValueHandler
import kotlin.reflect.KClass

/**
 * Defines the [InvalidCommandParameterValueHandler] for the [ch.leadrian.samp.kamp.core.api.command.Commands] class or the command function.
 * If no annotation is present, the default [InvalidCommandParameterValueHandler] or a custom provided one will be used.
 *
 * @see [ch.leadrian.samp.kamp.core.api.command.DefaultInvalidCommandParameterValueHandler]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class InvalidParameterValueHandler(val value: KClass<out InvalidCommandParameterValueHandler>)