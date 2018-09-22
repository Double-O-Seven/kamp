package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerKeyStateChangeListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerKeyStateChangeListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerKeyStateChangeListener>(OnPlayerKeyStateChangeListener::class), OnPlayerKeyStateChangeListener {

    override fun onPlayerKeyStateChange(oldKeys: ch.leadrian.samp.kamp.core.api.entity.PlayerKeys, newKeys: ch.leadrian.samp.kamp.core.api.entity.PlayerKeys) {
        getListeners().forEach {
            it.onPlayerKeyStateChange(oldKeys, newKeys)
        }
    }

}
