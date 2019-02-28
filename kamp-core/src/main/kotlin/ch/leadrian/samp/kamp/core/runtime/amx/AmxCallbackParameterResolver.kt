package ch.leadrian.samp.kamp.core.runtime.amx

import kotlin.reflect.KClass

internal interface AmxCallbackParameterResolver {

    fun resolve(parameterTypes: List<KClass<*>>, parameters: AmxCallbackParameters): Array<Any>

}