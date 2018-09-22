package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerDisconnectListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerDisconnectListener>(OnPlayerDisconnectListener::class), OnPlayerDisconnectListener {

    override fun onPlayerDisconnect(player: ch.leadrian.samp.kamp.core.api.entity.Player, reason: ch.leadrian.samp.kamp.core.api.constants.DisconnectReason): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerDisconnect(player, reason)
        }
    }

}
