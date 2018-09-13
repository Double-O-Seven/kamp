package ch.leadrian.samp.kamp.core.api.command.annotation

import ch.leadrian.samp.kamp.core.api.command.InvalidCommandParameterValueHandler
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class InvalidCommandParameterValueHandler(val value: KClass<out InvalidCommandParameterValueHandler>)