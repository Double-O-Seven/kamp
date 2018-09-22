package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnProcessTickListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnProcessTickListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnProcessTickListener>(OnProcessTickListener::class), OnProcessTickListener {

    override fun onProcessTick() {
        listeners.forEach {
            it.onProcessTick()
        }
    }

}
