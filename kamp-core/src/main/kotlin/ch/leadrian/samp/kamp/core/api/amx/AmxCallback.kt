package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.amx.AmxCallbackParameterResolver
import kotlin.reflect.KClass

abstract class AmxCallback
internal constructor(
        val name: String,
        private val amxCallbackParameterResolver: AmxCallbackParameterResolver,
        vararg parameterTypes: KClass<*>
) {

    private val parameterTypes: List<KClass<*>> = parameterTypes.toList()

    fun onPublicCall(paramsAddress: Int): Int {
        val parameterValues = amxCallbackParameterResolver.resolve(parameterTypes, paramsAddress)
        return onPublicCall(parameterValues)
    }

    internal abstract fun onPublicCall(parameterValues: Array<Any>): Int
}