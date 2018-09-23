package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerDisconnectHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerDisconnectListener>(OnPlayerDisconnectListener::class), OnPlayerDisconnectListener {

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        listeners.forEach {
            it.onPlayerDisconnect(player, reason)
        }
    }

}
