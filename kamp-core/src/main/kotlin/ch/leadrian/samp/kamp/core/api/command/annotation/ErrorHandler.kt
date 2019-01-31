package ch.leadrian.samp.kamp.core.api.command.annotation

import ch.leadrian.samp.kamp.core.api.command.CommandErrorHandler
import kotlin.reflect.KClass

/**
 * Defines the [CommandErrorHandler] for the [ch.leadrian.samp.kamp.core.api.command.Commands] class or the command function.
 * If no annotation is present, the default [CommandErrorHandler] or a custom provided one will be used.
 *
 * @see [ch.leadrian.samp.kamp.core.api.command.DefaultCommandErrorHandler]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class ErrorHandler(val value: KClass<out CommandErrorHandler>)