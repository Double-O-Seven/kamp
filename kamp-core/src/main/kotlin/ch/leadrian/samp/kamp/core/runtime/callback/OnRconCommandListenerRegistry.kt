package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnRconCommandListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnRconCommandListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnRconCommandListener>(OnRconCommandListener::class), OnRconCommandListener {

    override fun onRconCommand(command: kotlin.String): ch.leadrian.samp.kamp.core.api.callback.OnRconCommandListener.Result {
        getListeners().forEach {
            it.onRconCommand(command)
        }
    }

}
