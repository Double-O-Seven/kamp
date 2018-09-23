package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerKeyStateChangeListener
import ch.leadrian.samp.kamp.core.api.entity.PlayerKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerKeyStateChangeHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerKeyStateChangeListener>(OnPlayerKeyStateChangeListener::class), OnPlayerKeyStateChangeListener {

    override fun onPlayerKeyStateChange(oldKeys: PlayerKeys, newKeys: PlayerKeys) {
        listeners.forEach {
            it.onPlayerKeyStateChange(oldKeys, newKeys)
        }
    }

}
