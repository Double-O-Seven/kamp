package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnGameModeExitListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnGameModeExitListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnGameModeExitListener>(OnGameModeExitListener::class), OnGameModeExitListener {

    override fun onGameModeExit() {
        getListeners().forEach {
            it.onGameModeExit()
        }
    }

}
