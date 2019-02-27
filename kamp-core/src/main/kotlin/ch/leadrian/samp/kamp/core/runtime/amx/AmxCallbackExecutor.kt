package ch.leadrian.samp.kamp.core.runtime.amx

import ch.leadrian.samp.kamp.core.api.amx.AmxCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AmxCallbackExecutor
@Inject
constructor() {

    private val callbacksByName: MutableMap<String, AmxCallback> = mutableMapOf()

    fun onPublicCall(name: String, paramsAddress: Int): Int? {
        return callbacksByName[name]?.onPublicCall(paramsAddress)
    }

    fun register(callback: AmxCallback) {
        val previousCallback = callbacksByName.putIfAbsent(callback.name, callback)
        if (previousCallback != null) {
            throw IllegalStateException("Callback with name '${callback.name}' is already registered")
        }
    }

    fun unregister(callback: AmxCallback) {
        val removed = callbacksByName.remove(callback.name, callback)
        if (!removed) {
            throw IllegalArgumentException("Callback with name '${callback.name}' is not registered")
        }
    }
}