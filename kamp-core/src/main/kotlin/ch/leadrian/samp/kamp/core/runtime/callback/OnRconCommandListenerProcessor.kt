package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnRconCommandListener
import ch.leadrian.samp.kamp.core.api.callback.OnRconCommandListener.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnRconCommandListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnRconCommandListener>(OnRconCommandListener::class), OnRconCommandListener {

    override fun onRconCommand(command: String): Result {
        return listeners.map {
            it.onRconCommand(command)
        }.firstOrNull { it == Result.Processed } ?: Result.UnknownCommand
    }

}
