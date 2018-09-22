package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnGameModeInitListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnGameModeInitListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnGameModeInitListener>(OnGameModeInitListener::class), OnGameModeInitListener {

    override fun onGameModeInit() {
        listeners.forEach {
            it.onGameModeInit()
        }
    }

}
