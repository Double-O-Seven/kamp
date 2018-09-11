package ch.leadrian.samp.kamp.core.api.command

import kotlin.reflect.KClass

interface CommandParameterResolver<T : Any> {

    val parameterType: KClass<T>

    fun resolve(value: String): T

}