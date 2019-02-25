package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.amx.AmxCallbackParameterResolver
import kotlin.reflect.KClass

/**
 * Base class for AMX callbacks from other SA-MP plugins.
 *
 * @see [AmxCallback0]
 * @see [AmxCallback1]
 * @see [AmxCallback2]
 * @see [AmxCallback3]
 * @see [AmxCallback4]
 * @see [AmxCallback5]
 * @see [AmxCallback6]
 * @see [AmxCallback7]
 * @see [AmxCallback8]
 * @see [AmxCallback9]
 * @see [AmxCallback10]
 * @see [AmxCallback11]
 * @see [AmxCallback12]
 * @see [AmxCallback13]
 * @see [AmxCallback14]
 * @see [AmxCallback15]
 * @see [AmxCallback16]
 * @see [AmxCallback17]
 * @see [AmxCallback18]
 * @see [AmxCallback19]
 * @see [AmxCallback20]
 * @see [AmxCallback21]
 * @see [AmxCallback22]
 * @see [AmxCallback23]
 * @see [AmxCallback24]
 * @see [AmxCallback25]
 * @see [AmxCallback26]
 * @see [AmxCallback27]
 * @see [AmxCallback28]
 * @see [AmxCallback29]
 * @see [AmxCallback30]
 * @see [AmxCallback31]
 * @see [AmxCallback32]
 */
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