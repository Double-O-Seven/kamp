package ch.leadrian.samp.kamp.core.api.command.annotation

import ch.leadrian.samp.kamp.core.api.command.CommandErrorHandler
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class CommandErrorHandler(val value: KClass<out CommandErrorHandler>)