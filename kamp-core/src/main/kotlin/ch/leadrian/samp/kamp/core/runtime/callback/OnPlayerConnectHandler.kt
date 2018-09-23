package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerConnectListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerConnectHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerConnectListener>(OnPlayerConnectListener::class), OnPlayerConnectListener {

    override fun onPlayerConnect(player: Player) {
        listeners.forEach {
            it.onPlayerConnect(player)
        }
    }

}
