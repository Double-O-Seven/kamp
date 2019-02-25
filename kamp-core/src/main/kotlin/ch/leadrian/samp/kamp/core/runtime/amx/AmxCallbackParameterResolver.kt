package ch.leadrian.samp.kamp.core.runtime.amx

import kotlin.reflect.KClass

internal interface AmxCallbackParameterResolver {

    fun resolve(parameterTypes: List<KClass<*>>, paramsAddress: Int): Array<Any>

}